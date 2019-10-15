<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
    cancelText="关闭">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="父级用户" :labelCol="labelCol" :wrapperCol="wrapperCol" >
          <a-select
            style="width: 100%"
            placeholder="请选择父级用户"
            optionFilterProp = "children"
            v-decorator="['fid', {}]"
            >
            <a-select-option v-for="(user,userindex) in userList" :key="userindex.toString()" :value="user.id">
              {{ user.username }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <!--<a-form-item-->
          <!--:labelCol="labelCol"-->
          <!--:wrapperCol="wrapperCol"-->
          <!--label="父级id">-->
          <!--<a-input placeholder="请输入父级id" v-decorator="['fid', {}]" />-->
        <!--</a-form-item>-->
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="用户名">
          <a-input placeholder="请输入用户名" v-decorator="['username', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="密码">
          <a-input placeholder="请输入密码" v-decorator="['password', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="身份证号">
          <a-input placeholder="请输入身份证号" v-decorator="['idcard', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="所属部门">
          <a-input placeholder="请输入所属部门" v-decorator="['deptname', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="所属机构">
          <a-input placeholder="请输入所属机构" v-decorator="['affiliation', {}]" />
        </a-form-item>
        <a-form-item label="角色分配" :labelCol="labelCol" :wrapperCol="wrapperCol" >
          <a-select
            mode="multiple"
            style="width: 100%"
            placeholder="请选择用户角色"
            optionFilterProp = "children"
            v-model="selectedRole">
            <a-select-option v-for="(role,roleindex) in roleList" :key="roleindex.toString()" :value="role.id">
              {{ role.roleName }}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-spin>


  </a-modal>
</template>

<script>
 // import { httpAction } from '@/api/manage'
  import { getAction } from '@/api/manage'
  import {addUser,editUser,queryUserRole,queryall,queryallUsers } from '@/api/api'
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
    name: "UserModal",
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        roleList:[],    //下拉列表的值
        userList:[],    //下拉列表的值
        selectedRole:[],
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
          add: "/users/user/add",
          edit: "/users/user/edit",
        },
      }
    },
    created () {
      // const token = Vue.ls.get(ACCESS_TOKEN);
      // this.headers = {"X-Access-Token":token}
    },

    methods: {
      initialRoleList(){
        queryall().then((res)=>{
          if(res.success){
            this.roleList = res.result;
          }else{
           // console.log(res.message);
          }
        });
      },
      initialUserList(){
        queryallUsers().then((res)=>{
          if(res.success){
            this.userList = res.result;
          }else{
           // console.log(res.message);
          }
        });
      },
      loadUserRoles(userid){   //编辑时根据用户ID查询当前用户已拥有角色
        queryUserRole({userid:userid}).then((res)=>{
          if(res.success){
            this.selectedRole = res.result;
          }else{
            console.log(res.message);
          }
        });
      },


      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();


        let that = this;
        that.initialRoleList();
        that.initialUserList();
        if(record.hasOwnProperty("id")){
          that.loadUserRoles(record.id);
        }
        that.userId = record.id;




        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {  //表单显示默认值
          this.form.setFieldsValue(pick(this.model,'fid','username','password','idcard','deptname','affiliation'))
		  //时间格式化
        });

      },
      close () {
        this.$emit('close');
        this.visible = false;

        this.disableSubmit = false;
        this.selectedRole = [];
        //this.selectedFid= [];

      },
      moment,
      handleSubmit () {
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
            formData.selectedroles = this.selectedRole.length>0?this.selectedRole.join(","):'';
           // formData.selectedFids = this.selectedFid.length>0?this.selectedFid.join(","):'';

            //时间格式化

            let obj;
            if(!this.model.id){
              formData.id = this.userId;
              obj=addUser(formData);
            }else{
              obj=editUser(formData);
            }
            obj.then((res)=>{
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


    }
  }
</script>

<style lang="less" scoped>

</style>