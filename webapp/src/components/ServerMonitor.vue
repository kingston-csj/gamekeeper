<template>
  <div>
    <div>
      <el-select
        v-model="selected"
        placeholder="请选择服务器"
        :loading="loading">
        <el-option
          @click.native="searchClick(id)"
          v-for="item in servers"
          :key="item"
          :label="item"
          :value="item">
        </el-option>
      </el-select>
    </div>
    <div>
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="在线人数:">
          <el-input v-model="userInfo"></el-input>
        </el-form-item>
        <el-form-item label="内存信息:">
          <el-input v-model="memory"></el-input>
        </el-form-item>
      </el-form>  
    </div>
  </div>
</template>


<script>
  import {httpGet} from '../utils/httpclient'


  export default{
    data() {
      return {
        server: {},
        servers: [],
        loading: false,
        userInfo:"",
        memory:"",
        selected:0,
      }
    },
    mounted: function () {
      var _this = this;
      this.loading = true;
      httpGet('/server/serverIds', {
 
      }).then(resp=> {
          _this.loading = false;
          if (resp.status == 200) {
            _this.servers = resp.data.ids;
          }
      });
    },
    methods: {
      searchClick(id){
        httpGet('/server/monitor', {
 
        }).then(resp=> {
          this.loading = false;
          if (resp.status == 200) {
            //成功
            var json = resp.data;
            this.userInfo = json.userInfo;
            this.memory = json.memory
          } else {
            //失败
            this.$alert('失败2!', '失败!');
          }
        }, resp=> {
          this.loading = false;
          this.$alert('找不到服务器', '失败!');
        });
      },
    },
  }
</script>