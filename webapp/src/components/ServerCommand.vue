<template>
<div>
  <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
  <div style="margin: 15px 0;"></div>
  <el-checkbox-group v-model="checkedServers" @change="handleCheckedChange">
    <el-checkbox v-for="server in servers" :label="server" :key="server">{{server}}</el-checkbox>
  </el-checkbox-group>

  <el-button type="primary" size="small">执行</el-button>
</div>
</template>
<script>
  import {httpGet} from '../utils/httpclient'
  export default {
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
    data() {
      return {
        checkAll: false,
        checkedServers: [],
        servers: [],
        isIndeterminate: true
      };
    },
    methods: {
      handleCheckAllChange(val) {
        this.checkedServers = val ? this.servers : [];
        this.isIndeterminate = false;
      },
      handleCheckedChange(value) {
        let checkedCount = value.length;
        this.checkAll = checkedCount === this.servers.length;
        this.isIndeterminate = checkedCount > 0 && checkedCount < this.servers.length;
      }
    }
  };
</script>