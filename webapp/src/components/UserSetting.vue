<template>

<el-form-item label="可选角色">
    <el-select v-model="children" multiple placeholder="请选择">
        <el-option v-for="item in children" :key="item" :label="item" :value="item" ></el-option>
    </el-select> 
</el-form-item>

   
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
      searchClick(){
        this.loading = true;
        this.loadUserList();
      }
    },
    data(){
      return {
        loading: false,
        children:[],
      }
    }
  }
</script>
