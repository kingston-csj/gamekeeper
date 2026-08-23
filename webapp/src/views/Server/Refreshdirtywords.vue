<template>
    <div class="page-container" style="padding: 30px 15px;">
        <div class="page-title" style="font-size: 20px; font-weight: 600; margin-bottom: 20px;">屏蔽词更新</div>

        <!-- 功能说明区域 -->
        <div
            style="background: #f5f7fa; padding: 16px; border-radius: 6px; margin-bottom: 24px; color: #606266; line-height: 1.7;">
            <p style="font-weight: 600; margin:0 0 8px 0;">📌 功能说明</p>
            <p style="margin:0;">1. 支持拖拽本地TXT文件到下方上传区域，也可点击选择文件；</p>
            <p style="margin:0;">2. 文件仅支持 .txt 纯文本格式；</p>
            <p style="margin:0;">3. 上传后系统自动批量更新全量屏蔽词，旧数据会被覆盖；</p>
            <p style="margin:0;">4. 文本内一行一个敏感屏蔽词汇，无多余格式。</p>
            <p style="margin:0;">5. 词库更新只会对后续启动的服务器生效，若需立即生效，请使用服务器刷新屏蔽词功能。</p>
        </div>

        <!-- 拖拽上传区域 -->
        <div class="drag-box" :class="{ dragActive: dragOver }" @drop.prevent="onDropFile"
            @dragover.prevent="dragOver = true" @dragleave="dragOver = false">
            <i class="fa fa-file-text-o" style="font-size: 48px; color: #409eff;"></i>
            <p style="margin: 12px 0; font-size: 16px;">拖拽TXT文件到此区域</p>
            <p style="color: #999; margin:0 0 16px 0;">仅支持 txt 文本文件</p>
            <el-button type="primary" icon="fa fa-folder-open" @click="$refs.fileInput.click()">选择本地文件</el-button>
            <input ref="fileInput" type="file" accept=".txt" style="display: none;" @change="onSelectFile" />
        </div>

        <!-- 已选中文件展示 -->
        <div v-if="excelFile"
            style="margin-top: 18px; padding: 12px 16px; border: 1px solid #e4e7ed; border-radius: 6px; display: flex; align-items: center; justify-content: space-between;">
            <div style="display: flex; align-items: center;">
                <i class="fa fa-file-text-o" style="color: #67c23a; font-size: 18px;"></i>
                <span style="margin-left: 10px;">{{ excelFile.name }}</span>
            </div>
            <el-button icon="fa fa-times" type="text" style="color: #f56c6c;" @click="clearFile">移除文件</el-button>
        </div>

        <!-- 上传提交按钮 -->
        <div style="margin-top: 24px;">
            <el-button type="success" size="medium" icon="fa fa-upload" :loading="uploadLoading" :disabled="!excelFile"
                @click="submitUpload">
                确认上传更新屏蔽词
            </el-button>
            <el-button style="margin-left: 12px;" type="primary" size="medium" icon="fa fa-download"
                :loading="exportLoading" @click="exportWords">
                导出当前屏蔽词库
            </el-button>
        </div>
    </div>
</template>

<script>
export default {
    name: "SensitiveWordUpload",
    data() {
        return {
            dragOver: false,
            excelFile: null,
            uploadLoading: false,
            exportLoading: false
        }
    },
    methods: {
        // 拖拽释放文件
        onDropFile(e) {
            this.dragOver = false
            const fileList = e.dataTransfer.files
            if (!fileList.length) return
            this.checkFile(fileList[0])
        },
        // 点击选择文件
        onSelectFile(e) {
            const fileList = e.target.files
            if (!fileList.length) return
            this.checkFile(fileList[0])
        },
        // 文件格式校验（改为仅txt）
        checkFile(file) {
            const suffix = file.name.split(".").pop().toLowerCase()
            if (suffix !== "txt") {
                this.$message.error("仅支持 .txt 纯文本文件！")
                return
            }
            this.excelFile = file
        },
        // 清空选中文件
        clearFile() {
            this.excelFile = null
            this.$refs.fileInput.value = ""
        },
        // 提交上传到后端
        submitUpload() {
            if (!this.excelFile) return
            this.uploadLoading = true
            const formData = new FormData()
            formData.append("txtFile", this.excelFile)

            // 合并headers，保留token，仅补充multipart/form-data
            this.$api.server.uploadSensitiveWord(formData, {
                headers: {
                    "Content-Type": "multipart/form-data"
                }
            }).then(res => {
                this.uploadLoading = false
                if (res.code == 0) {
                    this.$message.success("屏蔽词更新成功！")
                    this.clearFile()
                } else {
                    this.$message.error("更新失败：" + res.desc)
                }
            }).catch(() => {
                this.uploadLoading = false
                this.$message.error("上传请求异常，请检查网络或文件")
            })
        },
        exportWords() {
            this.exportLoading = true
            this.$api.server.exportSensitiveWord().then(res => {
                const blob = res instanceof Blob
                    ? res
                    : new Blob([res], { type: "text/plain;charset=utf-8" })
                const fileName = "dirty_words_" + new Date().getTime() + ".txt"
                const downloadUrl = window.URL.createObjectURL(blob)
                const link = document.createElement("a")
                link.href = downloadUrl
                link.download = fileName
                document.body.appendChild(link)
                link.click()
                document.body.removeChild(link)
                window.URL.revokeObjectURL(downloadUrl)
                this.$message.success("屏蔽词库导出成功！")
            }).catch(() => {
                this.$message.error("屏蔽词库导出失败，请稍后重试")
            }).finally(() => {
                this.exportLoading = false
            })
        }
    }
}
</script>

<style scoped>
.drag-box {
    border: 2px dashed #dcdfe6;
    border-radius: 8px;
    padding: 50px 20px;
    text-align: center;
    transition: all 0.2s ease;
}

.drag-active {
    border-color: #409eff;
    background-color: #ecf5ff;
}
</style>
