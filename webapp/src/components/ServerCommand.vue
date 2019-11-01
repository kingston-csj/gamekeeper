<template>
<div>
  <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
  <div style="margin: 15px 0;"></div>
  <el-checkbox-group v-model="checkedServers" @change="handleCheckedChange">
    <el-checkbox v-for="server in servers" :label="server" :key="server">{{server}}</el-checkbox>
  </el-checkbox-group>

  <el-button type="primary" size="small" :disabled = "btnDisabled"  @click.native.prevent="submitClick">执行</el-button>
</div>
</template>
<script>
  import {httpGet} from '../utils/httpclient'
  import {httpPost} from '../utils/httpclient'  
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
        isIndeterminate: true,
        btnDisabled:false
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
      },
      submitClick() {
        this.btnDisabled = true;
        httpPost('/gameCmd/hotSwap', {
          selectedServers: this.checkedServers.join(";")
        }).then(resp=> {
          this.loading = false;
          this.btnDisabled = false;
          if (resp.status == 200) {
            //成功
            var json = resp.data;
            if (json.status == 'success') {
              this.$alert(json.msg, '执行结果');
            } else {
              this.$alert(json.msg, '失败!');
            }
          } else {
            //失败
            this.$alert('执行失败!', '失败!');
          }
        }, resp=> {
          this.loading = false;
          this.$alert('找不到服务器', '失败!');
        });

      }
    }
  };
</script>