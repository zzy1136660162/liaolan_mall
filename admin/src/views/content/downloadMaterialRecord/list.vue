<template>
  <div class="divBox">
    <el-card :bordered="false" shadow="never" class="ivu-mt" :body-style="{ padding: 0 }">
      <div class="padding-add">
        <el-form inline size="small">
          <el-form-item label="资料：">
            <el-select v-model="listPram.materialId" clearable class="selWidth" placeholder="请选择资料">
              <el-option v-for="item in materials" :key="item.id" :label="item.title" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="手机号：">
            <el-input v-model="listPram.phone" class="selWidth" clearable />
          </el-form-item>
          <el-form-item label="来源页：">
            <el-input v-model="listPram.sourcePage" class="selWidth" clearable />
          </el-form-item>
          <el-form-item label="开始日期：">
            <el-date-picker v-model="listPram.startDate" type="date" value-format="yyyy-MM-dd" placeholder="开始日期" />
          </el-form-item>
          <el-form-item label="结束日期：">
            <el-date-picker v-model="listPram.endDate" type="date" value-format="yyyy-MM-dd" placeholder="结束日期" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="small" @click="handerSearch">搜索</el-button>
            <el-button size="small" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="box-card mt14">
      <el-table v-loading="listLoading" :data="listData.list" size="mini" class="table" highlight-current-row>
        <el-table-column prop="id" label="ID" min-width="60" />
        <el-table-column prop="materialTitle" label="资料标题" min-width="220" />
        <el-table-column prop="companyName" label="公司名称" min-width="160" />
        <el-table-column prop="contactName" label="联系人" min-width="120" />
        <el-table-column prop="phone" label="手机号" min-width="140" />
        <el-table-column prop="sourcePage" label="来源页" min-width="140" />
        <el-table-column prop="requirementDesc" label="需求说明" min-width="240" show-overflow-tooltip />
        <el-table-column prop="createTime" label="提交时间" min-width="180" />
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
import { getDownloadMaterialList, getDownloadMaterialRecordList } from '@/api/downloadMaterial'

export default {
  data() {
    return {
      constants: this.$constants,
      listLoading: false,
      materials: [],
      listData: { list: [], total: 0 },
      listPram: {
        materialId: null,
        phone: '',
        sourcePage: '',
        startDate: '',
        endDate: '',
        page: 1,
        limit: this.$constants.page.limit[0]
      }
    }
  },
  mounted() {
    this.getMaterialOptions()
    this.handlerGetListData()
  },
  methods: {
    getMaterialOptions() {
      getDownloadMaterialList({ page: 1, limit: 999, status: -1 }).then((data) => {
        this.materials = data.list || []
      })
    },
    handlerGetListData() {
      this.listLoading = true
      getDownloadMaterialRecordList(this.listPram)
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
        materialId: null,
        phone: '',
        sourcePage: '',
        startDate: '',
        endDate: '',
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
