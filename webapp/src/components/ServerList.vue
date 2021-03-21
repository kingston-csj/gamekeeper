<template>
  <el-container class="server_list">
    <el-main class="main">
      <el-tabs v-model="activeName" @tab-click="handleClick" type="card">
        <el-tab-pane label="全部服务器" name="all">
          <server_table :showEdit="false"  :activeName="activeName"></server_table>
        </el-tab-pane>

      </el-tabs>
    </el-main>
  </el-container>
</template>
<script>
  import ServerTable from '@/components/ServerTable'
  import EmailCfg from '@/components/EmailCfg'
  import {httpGet} from '../utils/httpclient'

  export default {
    mounted: function () {
      var _this = this;
      httpGet("/isAdmin").then(resp=> {
        if (resp.status == 200) {
          _this.isAdmin = resp.data;
        }
      })
    },
    data() {
      return {
        activeName: 'post',
        isAdmin: false
      };
    },
    methods: {
      handleClick(tab, event) {
//        console.log(tab, event);
      }
    },
    components: {
      'server_table': ServerTable,
      'email_cfg': EmailCfg
    }
  };
</script>
<style>
  .server_list > .header {
    background-color: #ececec;
    margin-top: 10px;
    padding-left: 5px;
    display: flex;
    justify-content: flex-start;
  }

  .server_list > .main {
    display: flex;
    flex-direction: column;
    padding-left: 0px;
    background-color: #fff;
    padding-top: 0px;
    margin-top: 8px;
  }
</style>
