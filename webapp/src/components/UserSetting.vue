<template>
 <div style="width:600px">
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

  <el-form>
     <el-form-item v-if="visible" label="输入密码">
                <el-input type="password" v-model="newPassword" placeholder="请输入密码" style="width:200px">
                    <i slot="suffix" title="显示密码" @click="changePass('show')" style="cursor:pointer;"
                       class="el-icon-view"></i>
                </el-input>
            </el-form-item>
            <el-form-item v-else label="输入密码">
                <el-input type="text" v-model="newPassword" placeholder="请输入密码" style="width:200px">
                    <i slot="suffix" title="隐藏密码" @click="changePass('hide')" style="cursor:pointer;"
                       class="el-icon-circle-close-outline"></i>
                </el-input>
            </el-form-item>
  </el-form>


  <el-form>
   <el-form-item v-if="visible2" label="确认密码">
              <el-input type="password" v-model="newPassword2" placeholder="请输入密码" style="width:200px">
                  <i slot="suffix" title="显示密码" @click="changePass2('show')" style="cursor:pointer;"
                     class="el-icon-view"></i>
              </el-input>
          </el-form-item>
          <el-form-item v-else label="确认密码">
              <el-input type="text" v-model="newPassword2" placeholder="请输入密码" style="width:200px">
                  <i slot="suffix" title="隐藏密码" @click="changePass2('hide')" style="cursor:pointer;"
                     class="el-icon-circle-close-outline"></i>
              </el-input>
          </el-form-item>
  </el-form>

  <el-button type="primary" @click="submitEvent()">确定修改<i class="el-icon-upload el-icon--right"></i></el-button>


</div>
   
</template>


<script>
  import {httpGet} from '../utils/httpclient'
  import {httpPost} from '../utils/httpclient'
  export default{
    mounted: function () {
      this.loading = true;
      this.loadChannels();
    },
    methods: {
      loadChannels(){
        var _this = this;
        httpGet("/channel/children").then(resp=> {
          _this.loading = false;
          if (resp.status == 200) {
            _this.children = resp.data;
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

      changePass(value) {
        this.visible = !(value === 'show');
      },    //判断渲染，true:暗文显示，false:明文显示
            
      changePass2(value) {
        this.visible2 = !(value === 'show');
      },  
            

      submitEvent() {
        if (!this.chanelCode || this.chanelCode ==='') {
           this.$message({type: 'error', message: '请选择要修改密码的代理'});
           return;
        }
        if (!this.newPassword || !this.newPassword2) {
           this.$message({type: 'error', message: '密码不能为空'});
           return;
        }
        if (this.newPassword != this.newPassword2) {
           this.$message({type: 'error', message: '前后两次密码不一致，请检查'});
           return;
        }

        httpPost('/user/resetPwd', {
          targetUser: this.chanelCode,
          newPwd: this.newPassword
        }).then(resp=> {
          this.loading = false;
          if (resp.status == 200) {
            //成功
            var json = resp.data;
            if (json.status == 'success') {
                this.loading = false;
                this.$message({type: 'success', message: '修改对方密码成功'});
            } else {
              this.$alert(json.msg, '失败!');
            }
          } else {
            //失败
            this.$alert('登录失败2!', '失败!');
          }
        }, resp=> {
          this.loading = false;
          this.$alert('找不到服务器', '失败!');
        });

        this.loading = true;
      }
    },
    data(){
      return {
        loading: false,
        chanelCode:'',
        children:[],

        newPassword: '',
        newPassword2: '',

        visible: true,
        visible2:true
      }
    }
  }
</script>
