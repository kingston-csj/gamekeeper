<template>
<div>
  <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
  <div style="margin: 15px 0;"></div>
  <el-checkbox-group v-model="checkedServers" @change="handleCheckedChange">
    <el-checkbox v-for="server in servers" :label="server" :key="server">{{server}}</el-checkbox>
  </el-checkbox-group>


    <el-table
        ref="multipleTable"
        :data="commands"
        tooltip-effect="dark"
        style="overflow-x: hidden; overflow-y: hidden;" v-loading="loading">
        <el-table-column
          label="后台命令"
          width="200" align="left">
          <template slot-scope="scope"><span style="color: #409eff;cursor: pointer" @click="itemClick(scope.row)">{{ scope.row.name}}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="参数说明" width="400" align="left">
          <template slot-scope="scope">{{ scope.row.params}}</template>
        </el-table-column>
        <el-table-column
          label="参数" width="400" align="left">
          <template slot-scope="scope">
            <el-input placeholder="指令参数" @change="onCmdParamChanged($event, scope.row, scope.$index)"></el-input> 
         </template>

        </el-table-column>
        <el-table-column
          width="120" align="left">
          <template slot-scope="scope">
             <el-button type="primary" size="small" :disabled = "btnDisabled"  
             @click="cmdType=scope.row.type;submitClick()">执行</el-button>
          </template>
        </el-table-column>
    </el-table>


 
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
      this.loadCommands();
    },
    data() {
      return {
        checkAll: false,
        checkedServers: [],
        servers: [],
        commands:[],
        isIndeterminate: true,
        btnDisabled:false,
        loading:false,
        cmdParam:'',
        cmdType:1,
        params:[],
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

      loadCommands() {
        var _this = this;
        var url = "/gameCmd/commands"; 
      
        httpGet(url).then(resp=> {
          if (resp.status == 200) {
            this.commands = resp.data;
          } else {
            this.$message({type: 'error', message: '数据加载失败!'});
          }
        }, resp=> {

        }).catch(resp=> {

        })
      },

      onCmdParamChanged(param, row) {
          this.params[row.type] = param;
      },

      submitClick() {
        this.btnDisabled = true;
        httpPost('/gameCmd/exec', {
          selectedServers: this.checkedServers.join(";"),
          type:this.cmdType,
          params:this.params[this.cmdType]
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