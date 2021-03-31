<template>
<div>
  <div>
    <el-select
      v-model="selectedServerId"
      placeholder="请选择服务器"
      :loading="loading">
      <el-option
        v-for="item in servers"
        :key="item"
        :label="item"
        :value="item">
      </el-option>
    </el-select>
  </div>

  <div style="margin:10px 0 0 0;">
        <el-input type="text" v-model="querySign" placeholder="昵称或id" style="width: 20%"></el-input>
        <el-button type="primary" @click.native.prevent="queryPlayer" style="width: 10%">查询</el-button>
  </div>

  <div style="margin:20px 0 0 0;">
      <el-table
      :data="tableData"
      border
      style="width: 100%">
      <el-table-column
        prop="name"
        label="昵称"
        width="120">
      </el-table-column>
      <el-table-column
        prop="account"
        label="所属账号"
        width="120">
      </el-table-column>
      <el-table-column
        prop="level"
        label="等级"
        width="120">
      </el-table-column>
      <el-table-column
        prop="gold"
        label="金币"
        width="120">
      </el-table-column>
      <el-table-column
        prop="money"
        label="银币"
        width="300">
      </el-table-column>
      <el-table-column
        label="操作"
        width="100">
        <template slot-scope="scope">
          <el-button @click="banType=1;selectUid=scope.row.uid;cancelBanPlayer()" type="text" size="small" v-if="scope.row.banLogin > 0">解除封号</el-button>
          <el-button @click="banDialogVisible=true;banType=1;selectUid=scope.row.uid" type="text" size="small" v-if="scope.row.banLogin <= 0">封号</el-button>
          <el-button @click="banType=2;selectUid=scope.row.uid;cancelBanPlayer()" type="text" size="small" v-if="scope.row.banChat > 0">解除禁言</el-button>
          <el-button @click="banDialogVisible=true;banType=2;selectUid=scope.row.uid" type="text" size="small" v-if="scope.row.banChat <= 0">禁言</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <div>
    <el-dialog
      title="封禁确认"
      :visible.sync="banDialogVisible"
      width="30%">
      <el-form :inline="true" class="demo-form-inline"  :visible="false">
        <el-form-item>
          <el-date-picker v-model="endTime" type="datetime" placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="banDialogVisible=false">取 消</el-button>
        <el-button type="primary" @click="banPlayer()">确 定</el-button>
      </span>
    </el-dialog>
  </div>

</div>
</template>

<script>
  export default {
    methods: {
      searchServers() {
        var _this = this;

        this.$api.server.loadServerIds().then((res) => {
            _this.loading = false;
            _this.servers = res.ids;
        })     
      },
      queryPlayer() {
        var _this = this;    
        this.$api.player.queryPlayer({
          serverId: _this.selectedServerId,
          sign:_this.querySign
        }).then((res) => {
          _this.loading = false;
          _this.tableData = res.data;
        })

      }, 
      selectServerId(id) {
        this.selectedServerId = id;
      },
      banPlayer() {
        var _this = this;   
        _this.banDialogVisible=true;
        this.$api.player.banPlayer({
          serverId: _this.selectedServerId,
          endTime:this.endTime.getTime(),
          uid:this.selectUid,
          banType:this.banType
        }).then((res) => {
            _this.loading = false;
            _this.queryPlayer();
        })    
      },

      cancelBanPlayer() {
        this.$confirm('是否确定解除封禁?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.doCancelBan();
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });          
        });
      },
      doCancelBan() {
          this.$api.player.banPlayer({
            serverId: _this.selectedServerId,
            endTime:0,
            uid:this.selectUid,
            banType:this.banType
          }).then((res) => {
            _this.loading = false;
            this.queryPlayer();
        })
      }  
    },

    mounted: function () {
      this.searchServers();
    },

    data() {
      return {
        loading:false,
        servers:[],
        selectedServerId:0,
        tableData: [],
        querySign:'',
        banDialogVisible: false,
        endTime:'',
        banType:0, //1为封号；2为禁言
        selectUid:0
      }
    }
  }
</script>