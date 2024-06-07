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
            <!-- <el-input placeholder="指令参数" @change="onCmdParamChanged($event, scope.row, scope.$index)"></el-input>  -->
            <el-input  placeholder="指令参数" v-model="scope.row.real_params"></el-input>
         </template>

        </el-table-column>
        <el-table-column
          width="120" align="left">
          <template slot-scope="scope">
             <el-button type="primary" size="small" :disabled = "btnDisabled"  
             @click="cmdType=scope.row.type;cmdParam=scope.row.real_params;submitClick()">执行</el-button>
          </template>
        </el-table-column>
    </el-table>

 
</div>
</template>
<script>

  export default {
    mounted: function () {
      var _this = this;
      this.loading = true;
      this.$api.server.loadServerIds().then((res) => {
            _this.loading = false;
            _this.servers = res.ids;
        })
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
        this.$api.server.findCommands().then((res) => {
           this.commands = res;
        })
      },

      submitClick() {
        this.btnDisabled = true;
        this.$api.server.execCommand({
          selectedServers: this.checkedServers.join(";"),
          type:this.cmdType,
          params:this.cmdParam
        }).then((res) => {
          this.loading = false;
          this.btnDisabled = false;
          if(res.code == 200) {
            this.$alert(res.msg, '执行成功');
          } else {
            this.$alert(res.msg, '失败!');
          }
        
      })

      }
    }
  };
</script>