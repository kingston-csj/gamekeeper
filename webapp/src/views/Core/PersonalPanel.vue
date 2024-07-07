<template>
  
  <div class="personal-panel">
    <div class="personal-desc" :style="{'background':this.$store.state.app.themeColor}">
        <div class="avatar-container">
          <img class="avatar" :src="require('@/assets/user.jpeg')" />
        </div>  
        <div class="name-role">
          <span class="sender">{{ this.user_profile.name }} </span>  
        </div>  
        <!--<div class="registe-info">
          <span class="registe-info">
            <li class="fa fa-clock-o"></li>
            {{ user.registeInfo }}
          </span>
        </div>   -->
    </div>  

    <div class="main-operation">
        <span class="main-operation-item">
          <el-button size="small" icon="fa fa-male"> 个人中心 </el-button>
        </span>    
        <span class="main-operation-item">
          <el-button size="small" icon="fa fa-key" @click="dialogFormVisible = true"> 修改密码 </el-button>
           <div>
          <el-dialog title="修改密码" :visible.sync="dialogFormVisible" :append-to-body='true' width="250">
            <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
              <el-form-item label="旧密码" prop="oldPass">
                <el-input type="password" v-model="ruleForm.oldPass" autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item label="新密码" prop="newPass">
                <el-input type="password" v-model="ruleForm.newPass" autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item label="确认新密码" prop="checkNewPass">
                <el-input type="password" v-model="ruleForm.checkNewPass" autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
                <el-button @click="resetForm('ruleForm')">重置</el-button>
              </el-form-item>
            </el-form>
          </el-dialog>
    </div>  

        </span>    
    </div>
    <div class="other-operation">
       <!-- <div class="other-operation-item">
          <li class="fa fa-eraser"></li>
          清除缓存
        </div>    
        <div class="other-operation-item">
          <li class="fa fa-user"></li>
          在线人数
        </div>    -->
    </div>
    <div class="personal-footer" @click="logout">
      <li class="fa fa-sign-out"></li>
      {{$t("common.logout")}}
    </div>
  </div>
</template>

<script>
import Cookies from "js-cookie"
export default {
  name: 'PersonalPanel',
  components:{
  },
  props: {
    user: {
      type: Object,
      default: {
        name: "admin",
        avatar: "@/assets/user.jpeg"
      }
    }
  },
  data() {
      var validateOldPass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入原密码'));
        } else {
          if (this.ruleForm.checkPass !== '') {
            this.$refs.ruleForm.validateField('checkPass');
          }
          callback();
        }
      };
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else if (value.length < 6) {
            callback(new Error('密码长度最小为6'));
        } else {
          if (this.ruleForm.checkPass !== '') {
            this.$refs.ruleForm.validateField('checkPass');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.ruleForm.newPass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };

    return {
       user_profile: {
          name:''
       },
       dialogFormVisible: false,
       ruleForm: {
          oldPass: '',
          newPass: '',
          checkNewPass: ''
        },
        rules: {
          oldPass: [
            { validator: validateOldPass, trigger: 'blur' }
          ],
          newPass: [
            { validator: validatePass, trigger: 'blur' }
          ],
          checkNewPass: [
            { validator: validatePass2, trigger: 'blur' }
          ]
        }

    }
  },
  methods: {
    // 退出登录
    logout: function() {
      this.$confirm("确认退出吗?", "提示", {
        type: "warning"
      })
      .then(() => {
        sessionStorage.removeItem("user")
        this.deleteCookie("token")
        this.$router.push("/login")
        this.$api.login.logout().then((res) => {
          }).catch(function(res) {
        })
      })
      .catch(() => {})
    },
    // 删除cookie
    deleteCookie: function(name) { 
        Cookies.remove(name)
    },

    submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            var params = {
              'oldPass':this.ruleForm.oldPass,
              'newPass':this.ruleForm.newPass,
              'checkNewPass':this.ruleForm.checkNewPass
            };
            this.$api.user.modifyPass(params).then(res=>{
              if(res.code == 200) {
                this.$alert(res.msg, '执行成功');
                this.dialogFormVisible=false
              } else {
                this.$alert(res.msg, '失败!');
              }
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }  


  },
  mounted() {
    let userName = sessionStorage.getItem('user')
    this.user_profile.name = userName
  }
}
</script>

<style scoped>
.personal-panel {
  font-size: 14px;
  width: 280px;
  text-align: center;
  border-color: rgba(180, 190, 190, 0.2);
  border-width: 1px;
  border-style: solid;
  background: rgba(182, 172, 172, 0.1);
  margin: -14px;
}
.personal-desc {
  padding: 15px;
  color: #fff;
}
.avatar {
  width: 80px;
  height: 80px;
  border-radius: 90px;
}
.name-role {
  font-size: 16px;
  padding: 5px;
}
.personal-relation {
  font-size: 16px;
  padding: 12px;
  margin-right: 1px;
  background: rgba(200, 209, 204, 0.3);
}
.relation-item {
  padding: 12px;
}
.relation-item:hover {
  cursor: pointer;
  color: rgb(19, 138, 156);
}
.main-operation {
  padding: 8px;
  margin-right: 1px;
  /* background: rgba(175, 182, 179, 0.3); */
  border-color: rgba(201, 206, 206, 0.2);
  border-top-width: 1px;
  border-top-style: solid;
}
.main-operation-item {
  margin: 15px;
}
.other-operation {
  padding: 15px;
  margin-right: 1px;
  text-align: left;
  border-color: rgba(180, 190, 190, 0.2);
  border-top-width: 1px;
  border-top-style: solid;
}
.other-operation-item {
  padding: 12px;
}
.other-operation-item:hover {
  cursor: pointer;
  background: #9e94941e;
  color: rgb(19, 138, 156);
}
.personal-footer {
  margin-right: 1px;
  font-size: 14px;
  text-align: center;
  padding-top: 10px;
  padding-bottom: 10px;
  border-color: rgba(180, 190, 190, 0.2);
  border-top-width: 1px;
  border-top-style: solid;
}
.personal-footer:hover {
  cursor: pointer;
  color: rgb(19, 138, 156);
  background: #b1a6a61e;
}
</style>