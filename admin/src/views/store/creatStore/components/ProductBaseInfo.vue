<template>
  <el-row :gutter="24">
    <el-col v-bind="grid2">
      <el-form-item label="商品名称：" prop="storeName">
        <el-input
          class="from-ipt-width"
          v-model="formValidate.storeName"
          maxlength="249"
          placeholder="请输入商品名称"
          :disabled="isDisabled"
        />
      </el-form-item>
    </el-col>
    <el-col v-bind="grid2">
      <el-form-item label="商品分类：" prop="cateIds">
        <el-cascader
          class="from-ipt-width"
          v-model="formValidate.cateIds"
          :options="merCateList"
          :props="props2"
          clearable
          :show-all-levels="false"
          :disabled="isDisabled"
        />
      </el-form-item>
    </el-col>
    <el-col v-bind="grid2">
      <el-form-item label="商品关键字：" prop="keyword">
        <el-input
          class="from-ipt-width"
          v-model="formValidate.keyword"
          placeholder="请输入商品关键字"
          :disabled="isDisabled"
        />
      </el-form-item>
    </el-col>
    <el-col v-bind="grid2">
      <el-form-item label="单位：" prop="unitName">
        <el-input
          class="from-ipt-width"
          v-model="formValidate.unitName"
          placeholder="请输入单位"
          :disabled="isDisabled"
        />
      </el-form-item>
    </el-col>
    <el-col :span="24">
      <el-form-item label="商品封面图：" prop="image">
        <div class="acea-row upLoadPicBox row-middle">
          <div v-if="formValidate.image" class="pictrue" @click="$emit('openPicModal', '1')">
            <el-image
              class="image"
              :src="formValidate.image"
              :preview-src-list="isDisabled ? [formValidate.image] : []"
            >
            </el-image>
          </div>
          <div v-else class="upLoad" @click="$emit('openPicModal', '1')">
            <i class="el-icon-camera cameraIconfont" />
          </div>
        </div>
        <div class="from-tips" v-show="!isDisabled">建议尺寸：800*800px，上传小于500kb的图片</div>
      </el-form-item>
    </el-col>
    <el-col :span="24">
      <el-form-item label="商品轮播图：" prop="sliderImages">
        <div class="acea-row">
          <div
            v-for="(item, index) in formValidate.sliderImages"
            :key="index"
            class="pictrue"
            draggable="true"
            @dragstart="$emit('dragStart', $event, item)"
            @dragover.prevent="$emit('dragOver', $event, item)"
            @dragenter="$emit('dragEnter', $event, item)"
            @dragend="$emit('dragEnd', $event, item)"
          >
            <el-image class="image" :src="item" :preview-src-list="formValidate.sliderImages"> </el-image>
            <i v-if="!isDisabled" class="el-icon-error btndel" @click="$emit('removeSliderImage', index)" />
          </div>
          <div
            v-if="formValidate.sliderImages.length < 10 && !isDisabled"
            class="upLoadPicBox"
            @click="$emit('openPicModal', '2')"
          >
            <div class="upLoad">
              <i class="el-icon-camera cameraIconfont" />
            </div>
          </div>
        </div>
      </el-form-item>
    </el-col>
    <el-col>
      <el-form-item label="主图视频：">
        <el-input
          class="from-ipt-width mr15"
          maxlength="250"
          v-model="videoLink"
          :disabled="isDisabled"
          placeholder="请输入视频链接"
        />
        <el-button v-if="videoLink" @click="$emit('addVideo')" :disabled="isDisabled">确认添加</el-button>
        <el-button v-if="!videoLink" :disabled="isDisabled" @click="$emit('openPicModal', '3', '', '', 'video')"
          >选择视频</el-button
        >
      </el-form-item>
    </el-col>
    <el-col :span="24">
      <div class="iview-video-style" v-if="formValidate.videoLink">
        <video
          style="width: 100%; height: 100% !important; border-radius: 10px"
          :src="formValidate.videoLink"
          controls
          autoplay
          muted
        >
          您的浏览器不支持 video 标签。
        </video>
        <div class="mark"></div>
        <span class="iconv iconfont iconmd-trash" @click="$emit('delVideo')"></span>
      </div>
    </el-col>
    <el-col>
      <el-form-item label="运费模板：" prop="tempId">
        <el-select
          class="from-ipt-width mr15"
          v-model="formValidate.tempId"
          placeholder="请选择"
          :disabled="isDisabled"
          style="width: 100%"
        >
          <el-option v-for="item in shippingList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <el-button v-show="!isDisabled" class="mr15" @click="$emit('addTemplate')">运费模板</el-button>
      </el-form-item>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: 'ProductBaseInfo',
  props: {
    formValidate: {
      type: Object,
      required: true,
    },
    isDisabled: {
      type: Boolean,
      default: false,
    },
    merCateList: {
      type: Array,
      default: () => [],
    },
    shippingList: {
      type: Array,
      default: () => [],
    },
    props2: {
      type: Object,
      required: true,
    },
    ruleValidate: {
      type: Object,
      default: () => ({}),
    },
  },
  data() {
    return {
      grid2: {
        xl: 24,
        lg: 24,
        md: 24,
        sm: 24,
        xs: 24,
      },
    };
  },
  computed: {
    videoLink: {
      get() {
        return this.formValidate.videoLink;
      },
      set(val) {
        this.$emit('update:formValidate', { ...this.formValidate, videoLink: val });
      },
    },
  },
};
</script>

<style scoped lang="scss">
.pictrue {
  width: 60px;
  height: 60px;
  border: 1px dotted rgba(0, 0, 0, 0.1);
  margin-right: 10px;
  position: relative;
  cursor: pointer;

  img,
  .image {
    width: 100%;
    height: 100%;
  }

  video {
    width: 100%;
    height: 100%;
  }
}

.btndel {
  position: absolute;
  z-index: 1;
  width: 20px !important;
  height: 20px !important;
  left: 43px;
  top: 1px;
}

.iview-video-style {
  width: 300px;
  height: 180px;
  border-radius: 10px;
  background-color: #707070;
  margin: 0 120px 20px;
  position: relative;
  overflow: hidden;
}

.iview-video-style .iconv {
  color: #fff;
  line-height: 180px;
  width: 50px;
  height: 50px;
  display: inherit;
  font-size: 26px;
  position: absolute;
  top: -74px;
  left: 50%;
  margin-left: -25px;
}

.iview-video-style .mark {
  position: absolute;
  width: 100%;
  height: 30px;
  top: 0;
  background-color: rgba(0, 0, 0, 0.5);
  text-align: center;
}
</style>
