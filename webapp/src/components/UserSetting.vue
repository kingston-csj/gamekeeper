<template>
 <div>
   <el-select
      v-model="chanelCode"
      clearable>
      <el-option
       v-for="item in children"
        :key="item"
       :label="item"
       :value="item"
     ></el-option>
   </el-select>


    <el-form :model="resetForm" status-icon :rules="resetFormRules" ref="resetForm" label-width="100px">
      <el-form-item label="新密码" prop="newpwd">
          <el-input type="password" v-model="resetForm.newpwd" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="renewpwd">
          <el-input type="password" v-model="resetForm.renewpwd" auto-complete="off"></el-input>
      </el-form-item>
    </el-form>
</div>
   
</template>
<script>
  import {httpGet} from '../utils/httpclient'
  import {httpPut} from '../utils/httpclient'
  export default{
    mounted: function () {
      this.loading = true;
      this.loadUserList();
    },
    methods: {
      loadUserList(){
        var _this = this;
        httpGet("/channel/children").then(resp=> {
          _this.loading = false;
          if (resp.status == 200) {
            _this.users = resp.data;
          } else {
            _this.$message({type: 'error', message: '数据加载失败!'});
          }
        }, resp=> {
          _this.loading = false;
          if (resp.response.status == 403) {
            var data = resp.response.data;
            _this.$message({type: 'error', message: data});
          }
        });
      },
      validatePass(rule, value, callback)  {
          if (!value) {
            callback(new Error('请输入新密码'));
          } else if (value.toString().length < 6 || value.toString().length > 18) {
            callback(new Error('密码长度为6 - 18个字符'))
          } else {
            callback();
          }
        },
        validatePass2(rule, value, callback) {
            if (value === '') {
            callback(new Error('请再次输入密码'));
            } else if (value !== this.resetForm.newpwd) {
            callback(new Error('两次输入密码不一致!'));
            } else {
            callback();
            }
        },
             

      searchClick(){
        this.loading = true;
        this.loadUserList();
      }
    },
    data(){
      return {
        loading: false,
        chanelCode:'',
        children:[],
        resetForm: {
            newpwd: '',
            renewpwd: '',
        },
        resetFormRules: {
            newpwd: [
                { required: true, validator: validatePass, trigger: 'blur' }
            ],
            renewpwd: [
                { required: true, validator: validatePass2, trigger: 'blur' }
            ]
        },  
      }
    }
  }
</script>
