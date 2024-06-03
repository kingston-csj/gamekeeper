<template>
  <div style="float:left;padding-top:50px;">
    <el-upload
      action="/file/upload"
      :limit="5"
      :on-change="handleChange"
      accept=".jpg,.png,.jpeg"
      :auto-upload="false"
    >
    <template #trigger>
        <el-button type="primary">上传图片</el-button>
      </template>
    </el-upload>
<!-- 
    <el-table :data="picTable" style="width: 100%" size="medium">
        <el-table-column prop="picture" label=""  >
          <template slot-scope="scope">
            <img :src="scope.row.url" min-width="200" height="100"/>
          </template>
        </el-table-column>
        <el-table-column prop="name" label=""  >scope.row.name</el-table-column>
      </el-table> -->


    <div class="container">
      <div v-for="(pic, index) in picTable" :key="index">
           
            <img :src="pic.url" width="200" class="pic_item">
              {{ pic.name }}
             
      </div>
    </div>

  </div>
  </template>
<script>
  import axios from 'axios';
  import Cookies from "js-cookie";
  export default {
    data() {
      return {
        picTable: []
      };
    },
    mounted() {
      this.loadPictures()
    },
    methods: {
      handleChange(file) {
        let token = Cookies.get('token')
        const formData = new FormData();
        formData.append('file', file.raw);
        const headers= {
            'Content-Type': 'multipart/form-data'
          }
        this.$api.oss.uploadPic(formData, headers).then((res) => {
          if (res.code == 200) {
            this.loadPictures()
          }
        })
      },
      loadPictures() {
        this.$api.oss.queryAllPictures().then((res) => {
          this.picTable = res.data
           console.info("-=-queryAllPictures-----"+JSON.stringify(res))
        })
      }
    }  
  }
</script>

<style>
.container {
    max-width: 100%;
}

/* .pic_item {
  max-width: 250px;
  height: auto;
  display: inline-block;
} */



</style>