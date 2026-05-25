<template>
  <div class="divBox">
    <pages-header
      ref="pageHeader"
      :title="$route.params.id ? '编辑案例' : '添加案例'"
      backUrl="/content/caseManager"
    ></pages-header>
    <el-card class="box-card mt14">
      <div class="components-container">
        <el-form ref="pram" label-width="90px" :model="pram">
          <el-form-item
            label="标题："
            prop="title"
            :rules="[{ required: true, message: '请填写标题', trigger: ['blur', 'change'] }]"
          >
            <el-input v-model="pram.title" class="selWidth" placeholder="标题" maxlength="100" />
          </el-form-item>
          <el-form-item
            label="作者："
            prop="author"
            :rules="[{ required: true, message: '请填作者', trigger: ['blur', 'change'] }]"
          >
            <el-input class="selWidth" v-model="pram.author" placeholder="作者" maxlength="20" />
          </el-form-item>
          <el-form-item
            label="图文封面："
            prop="imageInput"
            :rules="[{ required: true, message: '请上传图文封面', trigger: 'change' }]"
          >
            <div class="upLoadPicBox" @click="modalPicTap('1')">
              <div v-if="pram.imageInput" class="pictrue"><img :src="pram.imageInput" /></div>
              <div v-else class="upLoad">
                <i class="el-icon-camera cameraIconfont" />
              </div>
            </div>
          </el-form-item>
          <el-form-item
            label="案例简介："
            prop="synopsis"
            :rules="[{ required: true, message: '请填写案例简介', trigger: ['blur', 'change'] }]"
          >
            <el-input
              v-model="pram.synopsis"
              maxlength="100"
              type="textarea"
              :rows="2"
              resize="none"
              placeholder="案例简介"
              class="selWidth"
            />
          </el-form-item>
          <el-divider content-position="left">案例扩展信息</el-divider>
          <el-form-item label="项目名称：">
            <el-input v-model="pram.projectName" class="selWidth" placeholder="请输入项目名称" maxlength="255" />
          </el-form-item>
          <el-form-item label="行业分类：">
            <el-select class="selWidth" v-model="pram.industryCategory" clearable placeholder="请选择行业分类">
              <el-option v-for="item in industryOptions" :key="item" :label="item" :value="item" />
            </el-select>
          </el-form-item>
          <el-form-item label="区域分类：">
            <el-select class="selWidth" v-model="pram.regionCategory" clearable placeholder="请选择区域分类">
              <el-option v-for="item in regionOptions" :key="item" :label="item" :value="item" />
            </el-select>
          </el-form-item>
          <el-form-item label="项目类型：">
            <el-input v-model="pram.projectType" class="selWidth" placeholder="请输入项目类型" maxlength="128" />
          </el-form-item>
          <el-form-item label="项目地点：">
            <el-input v-model="pram.projectAddress" class="selWidth" placeholder="请输入项目地点" maxlength="255" />
          </el-form-item>
          <el-form-item label="项目周期：">
            <el-input v-model="pram.projectPeriod" class="selWidth" placeholder="如：18个月、2025Q1" maxlength="128" />
          </el-form-item>
          <el-form-item label="项目背景：">
            <el-input v-model="pram.projectBackground" class="selWidth" type="textarea" :rows="3" placeholder="请输入项目背景" maxlength="500" />
          </el-form-item>
          <el-form-item label="供货产品：">
            <el-input v-model="pram.supplyProducts" class="selWidth" type="textarea" :rows="3" placeholder="请输入供货产品说明" maxlength="500" />
          </el-form-item>
          <el-form-item label="实施效果：">
            <el-input v-model="pram.implementationResult" class="selWidth" type="textarea" :rows="3" placeholder="请输入实施效果" maxlength="500" />
          </el-form-item>
          <el-form-item label="关联商品：">
            <el-input v-model="pram.productIds" class="selWidth" placeholder="商品ID，多个以逗号分隔，如：1,32,10" />
          </el-form-item>
          <el-form-item label="案例图片：">
            <el-input v-model="pram.coverImages" class="selWidth" type="textarea" :rows="2" placeholder="图片地址JSON数组，如：[&quot;https://a.jpg&quot;,&quot;https://b.jpg&quot;]" />
          </el-form-item>
          <el-form-item
            label="文章内容："
            prop="content"
            :rules="[{ required: true, message: '请填写文章内容', trigger: ['blur', 'change'] }]"
          >
            <Tinymce v-model="pram.content"></Tinymce>
          </el-form-item>
          <el-form-item label="是否Banner：">
            <el-switch v-model="pram.isBanner" />
          </el-form-item>
          <el-form-item label="是否热门：">
            <el-switch v-model="pram.isHot" />
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              :loading="loading"
              @click="handerSubmit('pram')"
              v-hasPermi="['admin:case:update']"
              >保存</el-button
            >
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script>
import Tinymce from '@/components/Tinymce/index';
import * as caseApi from '@/api/case.js';
import * as selfUtil from '@/utils/ZBKJIutil.js';
import { fileImageApi } from '@/api/systemSetting';
import { getToken } from '@/utils/auth';
import { Debounce } from '@/utils/validate';
export default {
  components: { Tinymce },
  data() {
    return {
      loading: false,
      constants: this.$constants,
      pram: {
        author: null,
        content: '',
        imageInput: '',
        isBanner: false,
        isHot: null,
        shareSynopsis: null,
        shareTitle: null,
        sort: 0,
        synopsis: null,
        title: null,
        url: null,
        id: null,
        projectName: '',
        industryCategory: '',
        regionCategory: '',
        projectType: '',
        projectAddress: '',
        projectPeriod: '',
        projectBackground: '',
        supplyProducts: '',
        implementationResult: '',
        productIds: '',
        coverImages: '',
      },
      industryOptions: ['能源领域', '建筑行业', '轨道交通', '电信通讯', '石油化工', '矿冶钢铁', '工业制造', '其他'],
      regionOptions: ['华东地区', '华南地区', '华北地区', '华中地区', '西南地区', '西北地区', '东北地区', '海外'],
      editData: {},
      myHeaders: { 'X-Token': getToken() },
      editorContentLaebl: ''
    };
  },
  created() {
    this.tempRoute = Object.assign({}, this.$route);
  },
  mounted() {
    if (this.$route.params.id) {
      this.getInfo();
      this.setTagsViewTitle();
    }
  },
  methods: {
    getInfo() {
      caseApi.getCaseDetail(this.$route.params.id).then((data) => {
        this.editData = data || {};
        this.hadlerInitEditData();
      });
    },
    modalPicTap(tit) {
      const _this = this;
      this.$modalUpload(
        function (img) {
          _this.pram.imageInput = img[0].sattDir;
        },
        tit,
        'content',
      );
    },
    hadlerInitEditData() {
      if (!this.$route.params.id) return;
      const {
        author,
        content,
        imageInput,
        isBanner,
        isHot,
        shareSynopsis,
        shareTitle,
        sort,
        synopsis,
        title,
        url,
        id,
        projectName,
        industryCategory,
        regionCategory,
        projectType,
        projectAddress,
        projectPeriod,
        projectBackground,
        supplyProducts,
        implementationResult,
        productIds,
        coverImages,
      } = this.editData;
      this.pram.author = author;
      this.pram.content = content;
      this.pram.imageInput = imageInput;
      this.pram.isBanner = isBanner;
      this.pram.isHot = isHot;
      this.pram.shareSynopsis = shareSynopsis;
      this.pram.shareTitle = shareTitle;
      this.pram.sort = sort;
      this.pram.synopsis = synopsis;
      this.pram.title = title;
      this.pram.url = url;
      this.pram.id = id;
      this.pram.projectName = projectName || '';
      this.pram.industryCategory = industryCategory || '';
      this.pram.regionCategory = regionCategory || '';
      this.pram.projectType = projectType || '';
      this.pram.projectAddress = projectAddress || '';
      this.pram.projectPeriod = projectPeriod || '';
      this.pram.projectBackground = projectBackground || '';
      this.pram.supplyProducts = supplyProducts || '';
      this.pram.implementationResult = implementationResult || '';
      this.pram.productIds = productIds || '';
      this.pram.coverImages = coverImages || '';
    },
    handerSubmit: Debounce(function (form) {
      this.$refs[form].validate((valid) => {
        if (!valid) return;
        if (!this.$route.params.id) {
          this.handlerSave();
        } else {
          this.handlerUpdate();
        }
      });
    }),
    handlerUpdate() {
      this.loading = true;
      this.pram.shareTitle = this.pram.title;
      this.pram.shareSynopsis = this.pram.synopsis;
      caseApi
        .updateCase(this.pram)
        .then((data) => {
          this.$message.success('编辑案例成功');
          this.loading = false;
          this.$router.push({ path: '/content/caseManager' });
        })
        .catch(() => {
          this.loading = false;
        });
    },
    handlerSave() {
      this.loading = true;
      this.pram.shareTitle = this.pram.title;
      this.pram.shareSynopsis = this.pram.synopsis;
      caseApi
        .saveCase(this.pram)
        .then((data) => {
          this.$message.success('新增案例成功');
          this.loading = false;
          this.$router.push({ path: '/content/caseManager' });
        })
        .catch(() => {
          this.loading = false;
        });
    },
    setTagsViewTitle() {
      const title = '编辑案例';
      const route = Object.assign({}, this.tempRoute, { title: `${title}-${this.$route.params.id}` });
      this.$store.dispatch('tagsView/updateVisitedView', route);
    },
  },
};
</script>

<style scoped>
.selWidth {
  width: 460px !important;
}
</style>
