<template>
  <div class="divBox">
    <pages-header
      ref="pageHeader"
      :title="$route.params.id ? '编辑文章' : '添加文章'"
      backUrl="/content/articleManager"
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
            label="文章分类："
            :rules="[{ required: true, message: '请选择分类', trigger: ['blur', 'change'] }]"
          >
            <el-select class="selWidth" v-model="pram.cid" placeholder="请选择">
              <el-option v-for="item in categoryTreeData" :key="item.id" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
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
            label="文章简介："
            prop="synopsis"
            :rules="[{ required: true, message: '请填写文章简介', trigger: ['blur', 'change'] }]"
          >
            <el-input
              v-model="pram.synopsis"
              maxlength="100"
              type="textarea"
              :rows="2"
              resize="none"
              placeholder="文章简介"
              class="selWidth"
            />
          </el-form-item>
          <el-divider content-position="left">案例扩展信息</el-divider>
          <el-form-item label="项目名称：">
            <el-input v-model="pram.caseInfo.projectName" class="selWidth" placeholder="请输入项目名称" maxlength="255" />
          </el-form-item>
          <el-form-item label="行业分类：">
            <el-select class="selWidth" v-model="pram.caseInfo.industryCategory" clearable placeholder="请选择行业分类">
              <el-option v-for="item in industryOptions" :key="item" :label="item" :value="item" />
            </el-select>
          </el-form-item>
          <el-form-item label="区域分类：">
            <el-select class="selWidth" v-model="pram.caseInfo.regionCategory" clearable placeholder="请选择区域分类">
              <el-option v-for="item in regionOptions" :key="item" :label="item" :value="item" />
            </el-select>
          </el-form-item>
          <el-form-item label="项目类型：">
            <el-input v-model="pram.caseInfo.projectType" class="selWidth" placeholder="请输入项目类型" maxlength="128" />
          </el-form-item>
          <el-form-item label="项目地点：">
            <el-input v-model="pram.caseInfo.projectAddress" class="selWidth" placeholder="请输入项目地点" maxlength="255" />
          </el-form-item>
          <el-form-item label="项目周期：">
            <el-input v-model="pram.caseInfo.projectPeriod" class="selWidth" placeholder="如：18个月、2025Q1" maxlength="128" />
          </el-form-item>
          <el-form-item label="项目背景：">
            <el-input v-model="pram.caseInfo.projectBackground" class="selWidth" type="textarea" :rows="3" placeholder="请输入项目背景" maxlength="500" />
          </el-form-item>
          <el-form-item label="供货产品：">
            <el-input v-model="pram.caseInfo.supplyProducts" class="selWidth" type="textarea" :rows="3" placeholder="请输入供货产品说明" maxlength="500" />
          </el-form-item>
          <el-form-item label="实施效果：">
            <el-input v-model="pram.caseInfo.implementationResult" class="selWidth" type="textarea" :rows="3" placeholder="请输入实施效果" maxlength="500" />
          </el-form-item>
          <el-form-item label="关联商品：">
            <el-input v-model="pram.caseInfo.productIds" class="selWidth" placeholder="商品ID，多个以逗号分隔，如：1,32,10" />
          </el-form-item>
          <el-form-item label="案例图片：">
            <el-input v-model="pram.caseInfo.coverImages" class="selWidth" type="textarea" :rows="2" placeholder="图片地址JSON数组，如：[&quot;https://a.jpg&quot;,&quot;https://b.jpg&quot;]" />
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
              v-hasPermi="['admin:article:update']"
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
import * as categoryApi from '@/api/categoryApi.js';
import * as articleApi from '@/api/article.js';
import { InfoArticleCase } from '@/api/article.js';
import * as selfUtil from '@/utils/ZBKJIutil.js';
import { fileImageApi } from '@/api/systemSetting';
import { getToken } from '@/utils/auth';
import { Debounce } from '@/utils/validate';
export default {
  // name: "edit",
  components: { Tinymce },
  data() {
    return {
      loading: false,
      constants: this.$constants,
      categoryTreeData: [],
      categoryProps: {
        value: 'id',
        label: 'name',
        children: 'child',
        expandTrigger: 'hover',
        checkStrictly: true,
        emitPath: false,
      },
      pram: {
        author: null,
        cid: null,
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
        caseInfo: {
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
    this.handlerGetCategoryTreeData();
  },
  methods: {
    getInfo() {
      InfoArticleCase(this.$route.params.id).then((data) => {
        this.editData = data.article || {};
        if (data.caseInfo) {
          this.pram.caseInfo = {
            projectName: data.caseInfo.projectName || '',
            industryCategory: data.caseInfo.industryCategory || '',
            regionCategory: data.caseInfo.regionCategory || '',
            projectType: data.caseInfo.projectType || '',
            projectAddress: data.caseInfo.projectAddress || '',
            projectPeriod: data.caseInfo.projectPeriod || '',
            projectBackground: data.caseInfo.projectBackground || '',
            supplyProducts: data.caseInfo.supplyProducts || '',
            implementationResult: data.caseInfo.implementationResult || '',
            productIds: data.caseInfo.productIds || '',
            coverImages: data.caseInfo.coverImages || '',
          };
        }
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
        cid,
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
      } = this.editData;
      this.pram.author = author;
      this.pram.cid = Number.parseInt(cid);
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
      // this.pram.mediaId = mediaId
    },
    handlerGetCategoryTreeData() {
      categoryApi.listCategroy({ type: 3, status: '' }).then((data) => {
        this.categoryTreeData = data;
        localStorage.setItem('adminArticleClassify', JSON.stringify(data));
      });
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
      this.pram.cid = Array.isArray(this.pram.cid) ? this.pram.cid[0] : this.pram.cid;
      this.pram.shareTitle = this.pram.title;
      this.pram.shareSynopsis = this.pram.synopsis;
      articleApi
        .UpdateArticle(this.pram)
        .then((data) => {
          this.$message.success('编辑文章成功');
          this.loading = false;
          this.$router.push({ path: '/content/articleManager' });
        })
        .catch(() => {
          this.loading = false;
        });
    },
    handlerSave() {
      this.loading = true;
      this.pram.cid = Array.isArray(this.pram.cid) ? this.pram.cid[0] : this.pram.cid;
      this.pram.shareTitle = this.pram.title;
      this.pram.shareSynopsis = this.pram.synopsis;
      articleApi
        .AddArticle(this.pram)
        .then((data) => {
          this.$message.success('新增文章成功');
          this.loading = false;
          this.$router.push({ path: '/content/articleManager' });
        })
        .catch(() => {
          this.loading = false;
        });
    },
    setTagsViewTitle() {
      const title = '编辑文章';
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
