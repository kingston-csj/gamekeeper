<template>
  <div>
    <div>
      <el-select v-model="selectedServerId" placeholder="请选择服务器" :loading="loading">
        <el-option v-for="item in servers" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
    </div>

    <div style="margin:10px 0 0 0;">
      <el-input type="text" v-model="querySign" placeholder="昵称或id" style="width: 20%"></el-input>
      <el-button type="primary" @click.native.prevent="handleSearch" style="width: 10%">查询</el-button>
    </div>

    <div style="margin:20px 0 0 0;">
      <el-table :data="tableData" border v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="id" width="120">
        </el-table-column>
        <el-table-column prop="name" label="公会名称" width="120">
        </el-table-column>

        <el-table-column prop="level" label="等级" width="120">
        </el-table-column>
        <el-table-column prop="memberCount" label="人数" width="300">
        </el-table-column>
        <el-table-column prop="contribution" label="贡献值" width="300">
        </el-table-column>
        <el-table-column prop="leader" label="会长" width="120">
        </el-table-column>
      </el-table>
    </div>

    <div style="padding: 10px 10px 10px 0; text-align: right;">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="pageRequest.pageNum" :page-sizes="[10, 20, 50]" :page-size="pageRequest.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
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
    getQueryParams() {
      return {
        serverId: this.selectedServerId,
        sign: this.querySign,
        pageNum: this.pageRequest.pageNum,
        pageSize: this.pageRequest.pageSize
      };
    },
    findPage() {
      if (!this.selectedServerId) {
        this.$message({ message: "请选择服务器", type: "warning" });
        return;
      }
      this.loading = true;
      this.$api.player.queryGuild(this.getQueryParams()).then((res) => {
        const data = res.data || {};
        this.tableData = data.content || [];
        this.total = data.totalSize || 0;
      }).finally(() => {
        this.loading = false;
      });
    },
    handleSearch() {
      this.pageRequest.pageNum = 1;
      this.findPage();
    },
    handleSizeChange(val) {
      this.pageRequest.pageSize = val;
      this.pageRequest.pageNum = 1;
      this.findPage();
    },
    handleCurrentChange(val) {
      this.pageRequest.pageNum = val;
      this.findPage();
    },
    selectServerId(id) {
      this.selectedServerId = id;
    },
  },

  mounted: function () {
    this.searchServers();
  },

  data() {
    return {
      loading: false,
      servers: [],
      selectedServerId: 0,
      tableData: [],
      querySign: '',
      selectUid: 0,
      total: 0,
      pageRequest: {
        pageNum: 1,
        pageSize: 10
      }
    }
  }
}
</script>