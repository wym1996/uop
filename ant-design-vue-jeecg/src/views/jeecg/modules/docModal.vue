<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
      
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="名称">
          <a-input placeholder="请输入名称" v-decorator="['docName', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="标准书号">
          <a-input placeholder="请输入标准书号" v-decorator="['isbn', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="类型">
          <a-select placeholder="请选择类型" v-decorator="['type', {}]" style="width: 120px">
          <a-select-option value="文档">文档</a-select-option>
          <a-select-option value="工具">工具</a-select-option>
        </a-select>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="版本">
          <a-input placeholder="请输入版本" v-decorator="['version', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="引用">
          <a-input placeholder="请输入引用" v-decorator="['reference', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="描述">
          <a-textarea placeholder="请输入描述" rows="10" v-decorator="['description', {}]" />
        </a-form-item>
        <a-form-item label="上传文件" :labelCol="labelCol" :wrapperCol="wrapperCol" v-has="'user:upload'">
          <a-upload
            name="file"
            :multiple="false"
            :action="uploadAction"
            :headers="headers"
            :data="{'isup':1,'bizPath':bizPath}"
            :fileList="fileList"
            :beforeUpload="beforeUpload"
            @change="handleChange">
            <a-button>
              <a-icon type="upload" />{{ text }}
            </a-button>
          </a-upload>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"

  import Vue from 'vue'
  import { ACCESS_TOKEN } from "@/store/mutation-types"

  const FILE_TYPE_ALL = "all"
  const FILE_TYPE_IMG = "image"
  const FILE_TYPE_TXT = "file"

  const uidGenerator=()=>{
    return '-'+parseInt(Math.random()*10000+1,10);
  }
  const getFileName=(path)=>{
    if(path.lastIndexOf("\\")>=0){
      let reg=new RegExp("\\\\","g");
      path = path.replace(reg,"/");
    }
    return path.substring(path.lastIndexOf("/")+1);
  }

  export default {
    name: "docModal",
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules:{
        },
        url: {
          add: "/doc/doc/add",
          edit: "/doc/doc/edit",
        },

        uploadAction:window._CONFIG['domianURL']+"/sys/common/upload",
        urlDownload:window._CONFIG['domianURL'] + "/sys/common/download/",
        headers:{},
        fileList: [],
        downloadpath:''
      }
    },

    props:{
      text:{
        type:String,
        required:false,
        default:"点击上传"
      },
      fileType:{
        type:String,
        required:false,
        default:FILE_TYPE_ALL
      },
      /*这个属性用于控制文件上传的业务路径*/
      bizPath:{
        type:String,
        required:false,
        default:"temp"
      },
      value:{
        type:String,
        required:false
      },
      //此属性被废弃了
      triggerChange:{
        type: Boolean,
        required: false,
        default: false
      },
    },
    watch:{
      value(val){
        this.initFileList(val)
      }
    },
    created(){
      const token = Vue.ls.get(ACCESS_TOKEN);
      this.headers = {"X-Access-Token":token}
    },

    methods: {
      add () {
        this.edit({});
        this.downloadpath = ''
      },

      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'docName', 'isbn', 'type', 'version', 'reference', 'description', 'downloadpath', 'delFlag'))
          //时间格式化
        });
      },

      initFileList(paths){
        if(!paths || paths.length==0){
          this.fileList = [];
          return;
        }
        let fileList = [];
        let arr = paths.split(",")
        for(var a=0;a<arr.length;a++){
          fileList.push({
            uid:uidGenerator(),
            name:getFileName(arr[a]),
            status: 'done',
            url: this.urlDownload+arr[a],
            response:{
              status:"history",
              message:arr[a]
            }
          })
        }
        this.fileList = fileList
      },

        handlePathChange(){
          let uploadFiles = this.fileList
          let path = ''
          if(!uploadFiles || uploadFiles.length==0){
            path = ''
          }
          let arr = [];

          for(var a=0;a<uploadFiles.length;a++){
            arr.push(uploadFiles[a].response.message)
          }
          if(arr.length>0){
            path = arr.join(",")
          }
          this.$emit('change', path);
        },

        beforeUpload(file){
          var fileType = file.type;
          if(fileType===FILE_TYPE_IMG){
            if(fileType.indexOf('image')<0){
              this.$message.warning('请上传图片');
              return false;
            }
          }else if(fileType===FILE_TYPE_TXT){
            if(fileType.indexOf('image')>=0){
              this.$message.warning('请上传文件');
              return false;
            }
          }
          //TODO 扩展功能验证文件大小
          return true
        },

        handleChange(info) {
          console.log("--文件列表改变--")
          let fileList = info.fileList
          fileList = fileList.slice(-1);
          if(info.file.status==='done'){
            if(info.file.response.success){
              fileList = fileList.map((file) => {
                if (file.response) {
                  file.url = this.urlDownload + file.response.message;
                  this.downloadpath = file.url;
                }
                return file;
              });
            }
            this.$message.success(`${info.file.name} 上传成功!`);
          }else if (info.file.status === 'error') {
            this.$message.error(`${info.file.name} 上传失败.`);
          }else if(info.file.status === 'removed'){
            this.handleDelete(info.file)
          }
          this.fileList = fileList
          if(info.file.status==='done' || info.file.status === 'removed'){
            this.handlePathChange()
          }
        },

        handleDelete(file){
          //如有需要新增 删除逻辑
          console.log(file)
        },

      close () {
        this.$emit('close');
        this.visible = false;
        this.fileList = [];
      },

      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }

            let formData = Object.assign(this.model, values);
            formData.downloadpath = this.downloadpath;
            //时间格式化
            console.log(formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
        })
      },
      handleCancel () {
        this.close()
      },
    },

    model: {
      prop: 'value',
      event: 'change'
    }
  }
</script>

<style lang="less" scoped>

</style>