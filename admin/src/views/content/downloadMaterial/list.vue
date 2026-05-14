<template>
  <div class="divBox">
    <el-card :bordered="false" shadow="never" class="ivu-mt" :body-style="{ padding: 0 }">
      <div class="padding-add">
        <el-form inline size="small">
          <el-form-item label="资料分类：">
            <el-select v-model="listPram.categoryId" clearable class="selWidth" placeholder="请选择资料分类">
              <el-option v-for="item in categoryTreeData" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态：">
            <el-select v-model="listPram.status" clearable class="selWidth" placeholder="请选择状态">
              <el-option label="全部" :value="-1" />
              <el-option label="启用" :value="1" />
              <el-option label="停用" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item label="关键词：">
            <el-input v-model="listPram.keywords" placeholder="标题/简介/文件类型" class="selWidth" clearable />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="small" @click="handerSearch">搜索</el-button>
            <el-button size="small" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="box-card mt14">
      <div slot="header" class="clearfix">
        <router-link :to="{ path: '/content/downloadMaterialCreate' }">
          <el-button v-hasPermi="['admin:download:material:save']" type="primary" class="mr10">新增资料</el-button>
        </router-link>
      </div>
      <el-table v-loading="listLoading" :data="listData.list" size="mini" class="table" highlight-current-row>
        <el-table-column prop="id" label="ID" min-width="60" />
        <el-table-column label="封面" min-width="90">
          <template slot-scope="scope">
            <el-image
              v-if="scope.row.coverImage"
              style="width: 36px; height: 36px"
              :src="scope.row.coverImage"
              :preview-src-list="[scope.row.coverImage]"
            />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="220" />
        <el-table-column prop="categoryName" label="分类" min-width="140" />
        <el-table-column prop="fileType" label="文件类型" min-width="100" />
        <el-table-column prop="downloadCount" label="下载次数" min-width="100" />
        <el-table-column prop="sort" label="排序" min-width="80" />
        <el-table-column label="展示/留资/状态" min-width="180">
          <template slot-scope="scope">
            <span>{{ scope.row.isPublic ? '公开' : '不公开' }}</span>
            <span> / </span>
            <span>{{ scope.row.needForm ? '需留资' : '直接下载' }}</span>
            <span> / </span>
            <span>{{ scope.row.status ? '启用' : '停用' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" min-width="180" />
        <el-table-column label="操作" width="100" fixed="right">
          <template slot-scope="scope">
            <router-link :to="{ path: '/content/downloadMaterialCreate/' + scope.row.id }">
              <a v-hasPermi="['admin:download:material:info']">编辑</a>
            </router-link>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :current-page="listPram.page"
        :page-sizes="constants.page.limit"
        :layout="constants.page.layout"
        :total="listData.total"
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
  </div>
</template>

<script>
import * as categoryApi from '@/api/categoryApi.js'
import { getDownloadMaterialList } from '@/api/downloadMaterial'

export default {
  data() {
    return {
      constants: this.$constants,
      listLoading: false,
      categoryTreeData: [],
      listData: { list: [], total: 0 },
      listPram: {
        categoryId: null,
        keywords: null,
        status: -1,
        page: 1,
        limit: this.$constants.page.limit[0]
      }
    }
  },
  mounted() {
    this.handlerGetTreeList()
    this.handlerGetListData()
  },
  methods: {
    handlerGetTreeList() {
      categoryApi.listCategroy({ type: 2, status: '' }).then((data) => {
        this.categoryTreeData = data
      })
    },
    handlerGetListData() {
      this.listLoading = true
      getDownloadMaterialList(this.listPram)
        .then((data) => {
          this.listData = data
        })
        .finally(() => {
          this.listLoading = false
        })
    },
    handerSearch() {
      this.listPram.page = 1
      this.handlerGetListData()
    },
    handleReset() {
      this.listPram = {
        categoryId: null,
        keywords: null,
        status: -1,
        page: 1,
        limit: this.$constants.page.limit[0]
      }
      this.handlerGetListData()
    },
    handleSizeChange(val) {
      this.listPram.limit = val
      this.handlerGetListData()
    },
    handleCurrentChange(val) {
      this.listPram.page = val
      this.handlerGetListData()
    }
  }
}
</script>

<style scoped>
.selWidth {
  width: 220px;
}
</style>
