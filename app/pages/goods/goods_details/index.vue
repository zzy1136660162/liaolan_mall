<template>
	<view :data-theme="theme">
		<skeleton :show="showSkeleton" :isNodes="isNodes" ref="skeleton" loading="chiaroscuro" selector="skeleton"
			bgcolor="#FFF"></skeleton>
		<view class="product-con skeleton" :style="{visibility: showSkeleton ? 'hidden' : 'visible'}">
			<view class='navbar' :class="opacity>0.6?'bgwhite':''">
				<view class='navbarH' :style='"height:"+navH+"rpx;"'>
					<view class='navbarCon acea-row' :style="{ paddingRight: navbarRight + 'px' }">
						<!-- #ifdef MP -->
						<view class="select_nav flex justify-center align-center" id="home" :style="{ top: homeTop + 'rpx' }">
							<text class="iconfont icon-fanhui2 px-20" @tap="returns"></text>
							<text class="iconfont icon-gengduo5 px-20" @tap="showNav"></text>
							<text class="nav_line"></text>
						</view>
						<!-- #endif -->
						<!-- #ifdef H5 || APP-PLUS -->
						<view id="home" class="home acea-row row-center-wrapper iconfont icon-xiangzuo h5_back"
							:class="opacity>0.5?'on':''" :style="{ top: homeTop + 'rpx' }" v-if="returnShow"
							@tap="returns">
						</view>
						<!-- #endif -->
						<!-- 头部tab标题 -->
						<!-- #ifdef H5  || APP-PLUS-->
						<view class="tab_nav" v-show="opacity > 0.6">
							<view class="header flex justify-between align-center">
								<view class="item" :class="navActive === index ? 'on' : ''" v-for="(item,index) in navList"
									:key='index' @tap="tap(index)">
									{{ item }}
								</view>
							</view>
						</view>
						<!-- #endif -->
						<!-- #ifdef H5 || APP-PLUS -->
						<view class="right_select" :style="{ top: homeTop + 'rpx' }" @tap="showNav">
							<text class="iconfont icon-gengduo2"></text>
						</view>
						<!-- #endif -->
					</view>
				</view>
			</view>
			<view class="dialog_nav" v-show="currentPage" :style="{ top: navH + 'rpx' }">
				<view class="dialog_nav_item" :class="item.after" v-for="(item,index) in selectNavList" :key="index"
					@click="linkPage(item.url)">
					<text class="iconfont" :class="item.icon"></text>
					<text class="pl-20">{{item.name}}</text>
				</view>
			</view>
			<view class="detail_container">
				<scroll-view :scroll-top="scrollTop" scroll-y='true' scroll-with-animation="true"
					:style='"height:"+height+"px;"' @scroll="scroll">
					<view id="past0">
						<!-- Hero 产品图 1:1 大图 -->
						<view class="hero-section skeleton-rect">
							<swiper class="hero-swiper" indicator-dots="true" indicator-color="rgba(255,255,255,0.5)"
								indicator-active-color="#0061a5" circular="true" autoplay="true" interval="4000" duration="500">
								<swiper-item v-for="(img, idx) in sliderImage" :key="idx">
									<image :src="img" mode="aspectFill" class="hero-image"></image>
								</swiper-item>
							</swiper>
							<view class="hero-tags" v-if="industryTags.length">
								<view class="hero-tag" v-for="(tag, idx) in industryTags" :key="idx">{{tag}}</view>
							</view>
						</view>
						<!-- 氛围图card -->
						<activity-style v-if="productInfo.activityStyle" :productInfo="productInfo"></activity-style>
						<view class="pad30">
							<!-- 产品信息卡片 -->
							<view class='product-info-card mb30 borRadius14'>
								<view class='info-header acea-row row-between row-bottom'>
									<view class='info-price-area skeleton-rect' v-if="!productInfo.activityStyle">
										<text class='price-symbol'>¥</text>
										<text class='price-value'>{{productInfo.price}}</text>
										<view class="vip-row" v-if="attr.productSelect.vipPrice && attr.productSelect.vipPrice > 0">
											<image :src="urlDomain+'crmebimage/perset/staticImg/vip_badge.png'" class="vip_icon"></image>
											<text class='vip_money skeleton-rect'>¥{{attr.productSelect.vipPrice}}</text>
										</view>
									</view>
									<view class='iconfont icon-fenxiang share-icon-btn' @click="listenerActionSheet"></view>
								</view>
								<view class='info-title skeleton-rect'>{{productInfo.storeName}}</view>
								<view class='info-desc skeleton-rect' v-if="productInfo.storeInfo">{{productInfo.storeInfo}}</view>
								<view class='info-meta acea-row row-between-wrapper'>
									<view class="skeleton-rect">原价:¥{{attr.productSelect.otPrice || 0}}</view>
									<view class="skeleton-rect">库存:{{productInfo.stock || 0}}{{productInfo.unitName || ''}}</view>
									<view class="skeleton-rect">销量:{{Math.floor(productInfo.sales) + Math.floor(productInfo.ficti) || 0}}{{productInfo.unitName || ''}}</view>
								</view>
								<view v-if="defaultCoupon.length>0 && type=='normal'" class='coupon acea-row row-between-wrapper' @click='couponTap'>
									<view class='hide line1 acea-row skeleton-rect'>优惠券：
										<view class='activity'>满{{defaultCoupon[0].minPrice}}减{{defaultCoupon[0].money}}</view>
									</view>
									<view class='iconfont icon-jiantou'></view>
								</view>
								<view class="coupon acea-row row-between-wrapper" v-if="activityH5.length">
									<view class="line1 acea-row">
										<text class="activityName skeleton-rect">活&nbsp;&nbsp;&nbsp;动：</text>
										<view v-for='(item,index) in activityH5' :key='index' @click="goActivity(item)" class="activityBox">
											<view v-if="item.type === '1'" class="skeleton-rect"
												:class="index==0?'activity_pin':'' || index==1?'activity_miao':'' || index==2?'activity_kan':''">
												<text class="iconfonts iconfont icon-miaosha1"></text>
												<text class="activity_title"> 参与秒杀</text>
											</view>
											<view class="skeleton-rect"
												:class="index==0?'activity_pin':'' || index==1?'activity_miao':'' || index==2?'activity_kan':''"
												v-if="item.type === '2'">
												<text class="iconfonts iconfont icon-kanjia"></text>
												<text class="activity_title"> 参与砍价</text>
											</view>
											<view class="skeleton-rect"
												:class="index==0?'activity_pin':'' || index==1?'activity_miao':'' || index==2?'activity_kan':''"
												v-if="item.type === '3'">
												<text class="iconfonts iconfont icon-pintuan"></text>
												<text class="activity_title"> 参与拼团</text>
											</view>
										</view>
									</view>
								</view>
							</view>
							<!-- 规格选择器 Bento卡片风格 -->
							<view class='spec-selector mb30 borRadius14 skeleton-rect' @click="selecAttr">
								<view class="spec-title-row acea-row row-middle">
									<view class="spec-title-bar"></view>
									<text class="spec-title-text">规格选择</text>
								</view>
								<view class="spec-group" v-for="(attrItem, attrIdx) in attr.productAttr" :key="attrIdx">
									<view class="spec-label">{{attrItem.attrName}}</view>
									<view class="spec-options" :class="attrItem.attrValues.length > 4 ? 'spec-grid-4' : 'spec-row'">
										<view
											class="spec-option"
											:class="{ active: attrItem.index === value }"
											v-for="(value, valIdx) in attrItem.attrValues"
											:key="valIdx">
											{{value}}
										</view>
									</view>
								</view>
								<view class="spec-sku-row acea-row row-between-wrapper" v-if="skuArr.length > 1">
									<view class="flex">
										<image :src="item.image" v-for="(item,index) in skuArr.slice(0,4)" :key="index" class="attrImg"></image>
									</view>
									<view class="switchTxt">共{{skuArr.length}}种规格可选</view>
								</view>
							</view>
							<!-- 用户评价 -->
							<view class='userEvaluation' id="past1">
								<view class='title acea-row row-between-wrapper'
									:style="replyCount==0?'border-bottom-left-radius:14rpx;border-bottom-right-radius:14rpx;':''">
									<view>用户评价<i>({{replyCount}})</i></view>
									<navigator class='praise' hover-class='none'
										:url='"/pages/goods/goods_comment_list/index?productId="+id'>
										<i>好评</i>&nbsp;<text class='font_color px-12'>{{replyChance || 0}}%</text>
										<text class='iconfont icon-jiantou'></text>
									</navigator>
								</view>
								<block v-if="replyCount">
									<userEvaluation :reply="reply"></userEvaluation>
								</block>
							</view>
							<!-- 技术性能指标表格 -->
							<view class="tech-table-section borRadius14" v-if="hasIndustryInfo" id="past1_5">
								<view class="tech-title-row acea-row row-middle">
									<view class="tech-title-bar"></view>
									<text class="tech-title-text">技术参数</text>
								</view>
								<view class="tech-table">
									<view class="tech-row" v-if="industryInfo.modelNo">
										<text class="tech-label">产品型号</text>
										<text class="tech-value">{{industryInfo.modelNo}}</text>
									</view>
									<view class="tech-row" v-if="industryInfo.voltageLevel">
										<text class="tech-label">电压等级</text>
										<text class="tech-value">{{industryInfo.voltageLevel}}</text>
									</view>
									<view class="tech-row" v-if="industryInfo.conductorMaterial">
										<text class="tech-label">导体材质</text>
										<text class="tech-value">{{industryInfo.conductorMaterial}}</text>
									</view>
									<view class="tech-row" v-if="industryInfo.coreCount">
										<text class="tech-label">芯数</text>
										<text class="tech-value">{{industryInfo.coreCount}}</text>
									</view>
									<view class="tech-row" v-if="industryInfo.crossSectionArea">
										<text class="tech-label">截面积</text>
										<text class="tech-value">{{industryInfo.crossSectionArea}}</text>
									</view>
									<view class="tech-row" v-if="industryInfo.sheathMaterial">
										<text class="tech-label">护套材质</text>
										<text class="tech-value">{{industryInfo.sheathMaterial}}</text>
									</view>
									<view class="tech-row" v-if="industryInfo.flameRetardantGrade">
										<text class="tech-label">阻燃等级</text>
										<text class="tech-value">{{industryInfo.flameRetardantGrade}}</text>
									</view>
									<view class="tech-row" v-if="industryInfo.standardCode">
										<text class="tech-label">执行标准</text>
										<text class="tech-value">{{industryInfo.standardCode}}</text>
									</view>
									<view class="tech-row" v-if="industryInfo.applicationScene">
										<text class="tech-label">适用场景</text>
										<text class="tech-value">{{industryInfo.applicationScene}}</text>
									</view>
								</view>
								<view class="tech-tags acea-row row-middle" v-if="industryTags.length">
									<view class="tech-tag" v-for="(tag, idx) in industryTags" :key="idx">{{tag}}</view>
								</view>
							</view>
							<!-- 产品结构详解 -->
							<view class="structure-section borRadius14" v-if="hasIndustryInfo" id="past1_6">
								<view class="tech-title-row acea-row row-middle">
									<view class="tech-title-bar"></view>
									<text class="tech-title-text">产品结构详解</text>
								</view>
								<view class="structure-body acea-row">
									<view class="structure-list">
										<view class="structure-item">
											<text class="structure-num">01</text>
											<view class="structure-text">
												<text class="structure-item-label">导体</text>
												<text class="structure-item-desc">{{industryInfo.conductorMaterial || '高纯度无氧铜'}}</text>
											</view>
										</view>
										<view class="structure-item">
											<text class="structure-num">02</text>
											<view class="structure-text">
												<text class="structure-item-label">绝缘层</text>
												<text class="structure-item-desc">交联聚乙烯（XLPE）绝缘</text>
											</view>
										</view>
										<view class="structure-item">
											<text class="structure-num">03</text>
											<view class="structure-text">
												<text class="structure-item-label">护套层</text>
												<text class="structure-item-desc">{{industryInfo.sheathMaterial || '聚氯乙烯（PVC）'}}</text>
											</view>
										</view>
									</view>
									<view class="structure-diagram" v-if="sliderImage.length">
										<image :src="sliderImage[0]" mode="aspectFit" class="structure-img"></image>
									</view>
								</view>
							</view>
							<!-- 优品推荐 -->
							<view class="superior borRadius14" v-if='good_list.length' id="past2">
								<view class="title acea-row row-center-wrapper">
									<image :src="urlDomain+'crmebimage/perset/staticImg/xzuo.png'"></image>
									<view class="titleTxt">优品推荐</view>
									<image :src="urlDomain+'crmebimage/perset/staticImg/xyou.png'"></image>
								</view>
								<view class="slider-banner banner">
									<swiper indicator-dots="true" :autoplay="autoplay" :circular="circular"
										:interval="interval" :duration="duration" indicator-color="#999"
										:indicator-active-color="indicatorBg" :style="'height:'+clientHeight+'px'">
										<swiper-item v-for="(item,indexw) in good_list" :key="indexw">
											<view class="list acea-row row-middle" :id="'list'+indexw">
												<view class="item" v-for="(val,indexn) in item.list" :key="indexn"
													@click="goDetail(val)">
													<view class="pictrue">
														<image :src="val.image"></image>
														<span class="pictrue_log pictrue_log_class"
															v-if="val.activityH5 && val.activityH5.type === '1'">秒杀</span>
														<span class="pictrue_log pictrue_log_class"
															v-if="val.activityH5 && val.activityH5.type === '2'">砍价</span>
														<span class="pictrue_log pictrue_log_class"
															v-if="val.activityH5 && val.activityH5.type === '3'">拼团</span>
													</view>
													<view class="name line1">{{val.storeName}}</view>
													<view class="money theme_price">¥{{val.price}}</view>
												</view>
											</view>
										</swiper-item>
									</swiper>
								</view>
							</view>
						</view>
					</view>
					<view class='product-intro' id="past3">
						<view class='title'>
							<image :src="urlDomain+'crmebimage/perset/staticImg/xzuo.png'"></image>
							<span class="sp">产品详情</span>
							<image :src="urlDomain+'crmebimage/perset/staticImg/xyou.png'"></image>
						</view>
						<view class='conter'>
							<jyf-parser :html="description" ref="article" :tag-style="tagStyle"></jyf-parser>
						</view>
					</view>
					<view class='bottom-spacer'></view>
				</scroll-view>
			</view>
			<!-- 底部操作栏 -->
			<view class='footer acea-row row-between-wrapper'>
				<view class="footer-left acea-row">
					<!-- #ifdef MP -->
					<button hover-class='none' class='footer-icon-btn' @click="onClickService"
						v-if="chatConfig.telephone_service_switch === 'open'">
						<view class='iconfont icon-kefu'></view>
						<view>客服</view>
					</button>
					<template v-else>
						<button open-type="contact" hover-class='none' class='footer-icon-btn' v-if="chatConfig.wx_chant_independent=='open'">
							<view class='iconfont icon-kefu'></view>
							<view>客服</view>
						</button>
						<button class="footer-icon-btn" hover-class='none' @click="wxChatService" v-else>
							<view class='iconfont icon-kefu'></view>
							<text>联系客服</text>
						</button>
					</template>
					<!-- #endif -->
					<!-- #ifndef MP -->
					<view class="footer-icon-btn" @click="onClickService">
						<view class="iconfont icon-kefu"></view>
						<view>客服</view>
					</view>
					<!-- #endif -->
					<view class="footer-icon-btn" @click="setCollect">
						<view class='iconfont icon-shoucang1' v-if="userCollect"></view>
						<view class='iconfont icon-shoucang' v-else></view>
						<view>收藏</view>
					</view>
				</view>
				<!-- 普通商品底部按钮 -->
				<block v-if="type === 'normal'">
					<view class="footer-right acea-row" v-if="attr.productSelect.stock <= 0">
						<view class="btn-outline" hover-class="none" @click="joinCart">加入采购清单</view>
						<view class="btn-disabled">已售罄</view>
					</view>
					<view class="footer-right acea-row" v-else>
						<view class="btn-outline" hover-class="none" @click="joinCart">加入采购清单</view>
						<view class="btn-primary" hover-class="none" @click="goBuy">立即咨询</view>
					</view>
				</block>
				<!-- 视频商品底部按钮 -->
				<view class="footer-right acea-row" v-if="attr.productSelect.stock <= 0 && type === 'video'">
					<view class="btn-disabled">已售罄</view>
				</view>
				<view class="footer-right acea-row" v-if="attr.productSelect.stock > 0 && type === 'video'">
					<view class="btn-primary" hover-class="none" @click="goBuy">立即咨询</view>
				</view>
			</view>
			<shareRedPackets :sharePacket="sharePacket" @listenerActionSheet="listenerActionSheet"
				@showShare="showShare"></shareRedPackets>
			<!-- 组件 -->
			<productWindow :attr="attr" :isShow='1' :iSplus='1' @myevent="onMyEvent" @ChangeAttr="ChangeAttr"
				@ChangeCartNum="ChangeCartNum" @attrVal="attrVal" @iptCartNum="iptCartNum" id='product-window'
				@getImg="showImg">
			</productWindow>
			<couponListWindow :coupon='coupon' :typeNum="couponDeaultType[0].useType"
				@ChangCouponsClone="ChangCouponsClone" @ChangCoupons="ChangCoupons"
				@ChangCouponsUseState="ChangCouponsUseState" @tabCouponType="tabCouponType"></couponListWindow>
			<!-- 分享按钮 -->
			<view class="generate-posters" :class="posters ? 'on' : ''">
				<view class="generateCon acea-row row-middle">
					<!-- #ifndef MP -->
					<button class="item" hover-class="none" v-if="weixinStatus === true" @click="H5ShareBox = true">
						<view class="pictrue">
							<image :src="urlDomain+'crmebimage/perset/staticImg/weixin.png'"></image>
						</view>
						<view class="">分享给好友</view>
					</button>
					<!-- #endif -->
					<!-- #ifdef MP -->
					<button class="item" open-type="share" hover-class="none">
						<view class="pictrue">
							<image :src="urlDomain+'crmebimage/perset/staticImg/weixin.png'"></image>
						</view>
						<view class="">分享给好友</view>
					</button>
					<!-- #endif -->
					<!-- #ifdef APP-PLUS -->
					<view class="item" @click="appShare('WXSceneSession')">
						<view class="iconfont icon-weixin3"></view>
						<view class="">微信好友</view>
					</view>
					<view class="item" @click="appShare('WXSenceTimeline')">
						<view class="iconfont icon-pengyouquan"></view>
						<view class="">微信朋友圈</view>
					</view>
					<!-- #endif -->
					<!-- #ifdef H5 || MP -->
					<view class="item" @click="getpreviewImage">
						<view class="pictrue">
							<image :src="urlDomain+'crmebimage/perset/staticImg/changan.png'"></image>
						</view>
						<view class="">预览发图</view>
					</view>
					<!-- #endif -->
					<!-- #ifdef MP  -->
					<button class="item" hover-class="none" @click="savePosterPath">
						<view class="pictrue">
							<image :src="urlDomain+'crmebimage/perset/staticImg/haibao.png'"></image>
						</view>
						<view class="">保存海报</view>
					</button>
					<!-- #endif -->
				</view>
				<view class="generateClose acea-row row-center-wrapper" @click="posterImageClose">取消</view>
			</view>
			<cus-previewImg ref="cusPreviewImg" :list="skuArr" @changeSwitch="changeSwitch"
				@shareFriend="listenerActionSheet" />
			<view class="mask" v-if="posters" @click="closePosters"></view>
			<view class="mask" v-if="canvasStatus"></view>
			<view class="mask_transparent" v-if="currentPage" @touchmove="hideNav" @click="hideNav()"></view>
			<!-- 海报展示 -->
			<view class='poster-pop' v-if="canvasStatus">
				<image :src='imagePath'></image>
			</view>
			<view class="canvas" v-else>
				<canvas style="width:750px;height:1190px;" canvas-id="firstCanvas"></canvas>
				<canvas canvas-id="qrcode" :style="{width: `${qrcodeSize}px`, height: `${qrcodeSize}px`}"/>
			</view>
			<!-- 发送给朋友图片 -->
			<view class="share-box" v-if="H5ShareBox">
				<image :src="urlDomain+'crmebimage/perset/staticImg/share-info.png'" @click="H5ShareBox = false"></image>
			</view>
		</view>
	</view>
</template>

<script>
	import activityStyle from "./components/activityStyle.vue";
	import uQRCode from '@/js_sdk/Sansnn-uQRCode/uqrcode.js'
	import store from '@/store';
	import {
		HTTP_H5_URL
	} from '@/config/app.js';
	import {
		spread
	} from "@/api/user";
	import {
		getProductDetail,
		collectAdd,
		collectDel,
		postCartAdd,
		getReplyList,
		getReplyConfig,
		getProductGood,
		getReplyProduct
	} from '@/api/store.js';
	import {
		getCoupons,
		tokenIsExistApi
	} from '@/api/api.js';
	import {
		getCartCounts
	} from '@/api/order.js';
	import {
		toLogin
	} from '@/libs/login.js';
	import {
		mapGetters
	} from "vuex";
	import {
		imageBase64
	} from "@/api/public";
	import productConSwiper from '@/components/productConSwiper';
	import couponListWindow from '@/components/couponListWindow';
	import productWindow from '@/components/productWindow';
	import userEvaluation from '@/components/userEvaluation';
	import shareRedPackets from '@/components/shareRedPackets';
	import cusPreviewImg from '@/components/cus-previewImg/cus-previewImg.vue'
	import {
		silenceBindingSpread
	} from "@/utils";
	import parser from "@/components/jyf-parser/jyf-parser";
	import {
		computeUser
	} from "@/api/user.js";
	// #ifdef MP
	import {
		base64src
	} from '@/utils/base64src.js'
	import {
		getQrcode
	} from '@/api/api.js';
	// #endif
	let app = getApp();
	import {
		setThemeColor
	} from '@/utils/setTheme.js'
	import {
		Debounce
	} from '@/utils/validate.js'
		import navBar from '@/components/navBar';
	export default {
		components: {
			productConSwiper,
			couponListWindow,
			productWindow,
			userEvaluation,
			shareRedPackets,
			cusPreviewImg,
			"jyf-parser": parser,
			activityStyle,
			navBar
		},
		data() {
			let that = this;
			return {
				urlDomain: this.$Cache.get("imgHost"),
				showSkeleton: true,
				isNodes: 0,
				coupon: {
					coupon: false,
					type: 0,
					list: [],
					count: []
				},
				attrTxt: '请选择',
				attrValue: '',
				animated: false,
				id: 0,
				replyCount: 0,
				reply: [],
				productInfo: {},
				productValue: [],
				industryInfo: {
					modelNo: '',
					voltageLevel: '',
					conductorMaterial: '',
					coreCount: '',
					crossSectionArea: '',
					sheathMaterial: '',
					flameRetardantGrade: '',
					standardCode: '',
					applicationScene: '',
					certificateFiles: [],
					downloadFileIds: [],
				},
				couponList: [],
				cart_num: 1,
				isAuto: false,
				isShowAuth: false,
				isOpen: false,
				actionSheetHidden: true,
				storeImage: '',
				PromotionCode: '',
				posterbackgd: `${this.$Cache.get("imgHost")}crmebimage/perset/staticImg/posterbackgd.png`,
				sharePacket: {
					isState: true,
					touchstart: false
				},
				circular: false,
				autoplay: false,
				interval: 3000,
				duration: 500,
				clientHeight: "",
				systemStore: {},
				good_list: [],
				replyChance: 0,
				CartCount: 0,
				isDown: true,
				posters: false,
				weixinStatus: false,
				attr: {
					cartAttr: false,
					productAttr: [],
					productSelect: {}
				},
				description: '',
				navActive: 0,
				H5ShareBox: false,
				activityH5: [],
				retunTop: true,
				navH: "",
				navList: [],
				opacity: 0,
				scrollY: 0,
				topArr: [],
				toView: '',
				height: 0,
				heightArr: [],
				lock: false,
				scrollTop: 0,
				tagStyle: {
					img: 'width:100%;display:block;',
					table: 'width:100%',
					video: 'width:100%'
				},
				sliderImage: [],
				videoLink: '',
				qrcodeSize: 600,
				canvasStatus: false,
				imagePath: '',
				imgTop: '',
				errT: '',
				homeTop: 20,
				navbarRight: 0,
				userCollect: false,
				returnShow: true,
				type: "", 
				theme: app.globalData.theme,
				indicatorBg: '',
				shareStatus: true,
				skuArr: [],
				currentPage: false,
				selectSku: {},
				selectNavList: [{
						name: '首页',
						icon: 'icon-shouye8',
						url: '/pages/index/index',
						after: 'dialog_after'
					},
					{
						name: '搜索',
						icon: 'icon-sousuo6',
						url: '/pages/goods/goods_search/index',
						after: 'dialog_after'
					},
					{
						name: '购物车',
						icon: 'icon-gouwuche7',
						url: '/pages/order_addcart/order_addcart',
						after: 'dialog_after'
					},
					{
						name: '我的收藏',
						icon: 'icon-shoucang3',
						url: '/pages/users/user_goods_collection/index',
						after: 'dialog_after'
					},
					{
						name: '个人中心',
						icon: 'icon-gerenzhongxin1',
						url: '/pages/user/index'
					},
				],
				chatConfig: {
					consumer_hotline: '',
					telephone_service_switch: 'close',
					wx_chant_independent:'open'
				},
				defaultCoupon: [],
				couponDeaultType: [{
					useType: 1
				}],
				where: {
					page: 1,
					limit: 999,
					productId: 0,
					type: 0
				},
				couponType: 0,
			};
		},
		computed: {
			...mapGetters(['isLogin', 'uid', 'chatUrl', 'productType']),
			hasIndustryInfo() {
				const info = this.industryInfo;
				return !!(info.modelNo || info.voltageLevel || info.conductorMaterial ||
					info.coreCount || info.crossSectionArea || info.sheathMaterial ||
					info.flameRetardantGrade || info.standardCode || info.applicationScene);
			},
			industryTags() {
				const tags = [];
				if (this.industryInfo.flameRetardantGrade) tags.push(this.industryInfo.flameRetardantGrade);
				if (this.industryInfo.conductorMaterial) tags.push(this.industryInfo.conductorMaterial);
				if (this.industryInfo.sheathMaterial) tags.push(this.industryInfo.sheathMaterial);
				return tags;
			},
		},
		watch: {
			productInfo: {
				handler: function() {
					this.$nextTick(() => {});
				},
				immediate: true
			}
		},
		onShow() {
			this.getTokenIsExist();
		},
		onLoad(options) {
			this.$set(this, 'theme', this.$Cache.get('theme'));

			var pages = getCurrentPages();
			this.returnShow = pages.length === 1 ? false : true;
			if (pages.length <= 1) {
				this.retunTop = false
			}

			let that = this;
			uni.getSystemInfo({
				success: function(res) {
					that.height = res.windowHeight
				},
			});

			if (!options.scene && !options.id) {
				this.showSkeleton = false;
				this.$util.Tips({
					title: '缺少参数无法查看商品'
				}, {
					url: '/pages/index/index'
				});
				return;
			}
			
			if (options.id) {
				this.id = parseInt(options.id);
				if (isNaN(this.id) || this.id <= 0) {
					this.showSkeleton = false;
					this.$util.Tips({
						title: '商品参数错误'
					}, {
						url: '/pages/index/index'
					});
					return;
				}
			}
			
			if (options.scene) {
				let qrCodeValue = this.$util.getUrlParams(decodeURIComponent(options.scene));
				let mapeMpQrCodeValue = this.$util.formatMpQrCodeData(qrCodeValue);
				app.globalData.spread = mapeMpQrCodeValue.spread;
				this.id = mapeMpQrCodeValue.id;
				this.type = mapeMpQrCodeValue.type ? mapeMpQrCodeValue.type : 'normal';
			}
			if (options.type === 'video') {
				// #ifdef MP
				this.navH = 160;
				// #endif
				this.type = options.type
			} else {
				this.type = 'normal'
				this.navH = app.globalData.navHeight;
			}
			this.$store.commit("PRODUCT_TYPE", this.type);
			this.$set(this, 'chatConfig', this.$Cache.getItem('chatConfig'));
			// #ifdef H5
			computeUser();
			// #endif

			if (options.spread) this.$Cache.set('spread',options.spread);

			this.getGoodsDetails();
			this.getCouponType();
			this.getProductReplyList();
			this.getProductReplyCount();
			this.getGoods();
			this.indicatorBg = setThemeColor();
		},
		onReady() {
			this.isNodes++;
			this.$nextTick(function() {
				// #ifdef MP
				const menuButton = uni.getMenuButtonBoundingClientRect();
				const query = uni.createSelectorQuery().in(this);
				query
					.select('#home')
					.boundingClientRect(data => {
						this.homeTop = menuButton.top * 2 + menuButton.height - data.height;
					})
					.exec();
				// #endif
				// #ifdef APP-PLUS
				this.homeTop = 60;
				// #endif
			});
		},
		// #ifdef MP
		onShareAppMessage: function(res) {
			let that = this;
			that.$set(that, 'actionSheetHidden', !that.actionSheetHidden);
			return {
				title: that.productInfo.storeName || '',
				imageUrl: that.productInfo.image || '',
				path: '/pages/goods/goods_details/index?id=' + that.id + '&spread=' + that.uid,
			}
		},
		// #endif
		onReachBottom() {
			this.getCouponList(this.couponType);
		},
		onPageScroll(e) {
			uni.$emit('scroll');
		},
		methods: {
			wxChatService(){
				let chatUrlArr = this.chatUrl.split('?')
				uni.navigateTo({
					url:`/pages/users/web_page/index?webUel=${chatUrlArr[0]}&title=客服&${chatUrlArr[1]}`
				})
			},
			getTokenIsExist() {
				this.$LoginAuth.getTokenIsExist().then(data => {
					if (data) {
						this.getCouponList(1);
						this.getCartCount(true);
						silenceBindingSpread();
					}
				});
			},
			// #ifdef APP-PLUS
			appShare(scene) {
				let that = this
				let routes = getCurrentPages();
				let curRoute = routes[routes.length - 1].$page.fullPath
				uni.share({
					provider: "weixin",
					scene: scene,
					type: 0,
					href: `${HTTP_H5_URL}${curRoute}&spread=${that.uid}`,
					title: that.productInfo.storeName,
					summary: app.globalData.companyName,
					imageUrl: that.productInfo.image,
					success: function(res) {
						that.posters = false;
					},
					fail: function(err) {
						uni.showToast({
							title: '分享失败',
							icon: 'none',
							duration: 2000
						})
						that.posters = false;
					}
				});
			},
			// #endif
			onClickService() {
				if (this.chatConfig.telephone_service_switch === 'open') {
					uni.makePhoneCall({
						phoneNumber: this.chatConfig.consumer_hotline
					});
				} else {
					// #ifdef APP-PLUS
					uni.navigateTo({
						url: '/pages/users/web_page/index?webUel=' + this.chatUrl + '&title=客服'
					})
					// #endif
					// #ifndef APP-PLUS
					location.href = this.chatUrl;
					// #endif
				}
			},
			goActivity: function(e) {
				let item = e;
				if (item.type === "1") {
					uni.navigateTo({
						url: `/pages/activity/goods_seckill_details/index?id=${item.id}`
					});
				} else if (item.type === "2") {
					uni.navigateTo({
						url: `/pages/activity/goods_bargain_details/index?id=${item.id}&startBargainUid=${this.uid}`
					});
				} else {
					uni.navigateTo({
						url: `/pages/activity/goods_combination_details/index?id=${item.id}`
					});
				}
			},
			iptCartNum: function(e) {
				this.$set(this.attr.productSelect, 'cart_num', e ? e : 1);
			},
			returns: function() {
				uni.navigateBack()
			},
			showNav() {
				this.currentPage = !this.currentPage;
			},
			tap: function(index) {
				var id = "past" + index;
				var index = index;
				var that = this;
				this.$set(this, 'toView', id);
				this.$set(this, 'navActive', index);
				this.$set(this, 'lock', true);
				this.$set(this, 'scrollTop', index > 0 ? that.topArr[index] - (app.globalData.navHeight / 2) : that
					.topArr[index]);
			},
			scroll: function(e) {
				var that = this,
					scrollY = e.detail.scrollTop;
				var opacity = scrollY / 500;
				opacity = opacity > 1 ? 1 : opacity;
				that.$set(that, 'opacity', opacity);
				that.$set(that, 'scrollY', scrollY);
				if (that.lock) {
					that.$set(that, 'lock', false)
					return;
				}
				for (var i = 0; i < that.topArr.length; i++) {
					if (scrollY < that.topArr[i] - (app.globalData.navHeight / 2) + that.heightArr[i]) {
						that.$set(that, 'navActive', i)
						break
					}
				}
				that.$set(that.sharePacket, 'touchstart', true);
			},
			goDetail(item) {
				if (!item.activityH5) {
					uni.redirectTo({
						url: '/pages/goods/goods_details/index?id=' + item.id
					})
					return
				}
				if (item.activityH5.length == 0) {
					uni.redirectTo({
						url: '/pages/goods/goods_details/index?id=' + item.id
					})
					return
				}
				if (item.activityH5 && item.activityH5.type == 2) {
					uni.redirectTo({
						url: `/pages/activity/goods_bargain_details/index?id=${item.activityH5.id}&bargain=${this.uid}`
					})
					return
				}
				if (item.activityH5 && item.activityH5.type == 3) {
					uni.redirectTo({
						url: `/pages/activity/goods_combination_details/index?id=${item.activityH5.id}`
					})
					return
				}
				if (item.activityH5 && item.activityH5.type == 1) {
					debugger
					uni.redirectTo({
						url: `/pages/activity/goods_seckill_details/index?id=${item.activityH5.id}`
					})
					return
				}
			},
			ChangCouponsClone: function() {
				this.$set(this.coupon, 'coupon', false)
			},
			ChangeCartNum: function(changeValue) {
				let productSelect = this.productValue[this.attrValue];
				if (productSelect === undefined && !this.attr.productAttr.length)
					productSelect = this.attr.productSelect;
				if (productSelect === undefined) return;
				let stock = productSelect.stock || 0;
				let num = this.attr.productSelect;
				if (changeValue) {
					num.cart_num++;
					if (num.cart_num > stock) {
						this.$set(this.attr.productSelect, "cart_num", stock);
						this.$set(this, "cart_num", stock);
					}
				} else {
					num.cart_num--;
					if (num.cart_num < 1) {
						this.$set(this.attr.productSelect, "cart_num", 1);
						this.$set(this, "cart_num", 1);
					}
				}
			},
			attrVal(val) {
				this.$set(this.attr.productAttr[val.indexw], 'index', this.attr.productAttr[val.indexw].attrValues[val
					.indexn]);
			},
			ChangeAttr: function(res) {
				let productSelect = this.productValue[res];
				this.$set(this, "selectSku", productSelect);
				if (productSelect) {
					this.$set(this.attr.productSelect, "image", productSelect.image);
					this.$set(this.attr.productSelect, "price", productSelect.price);
					this.$set(this.attr.productSelect, "stock", productSelect.stock);
					this.$set(this.attr.productSelect, "unique", productSelect.id);
					this.$set(this.attr.productSelect, "cart_num", 1);
					this.$set(this.attr.productSelect, "vipPrice", productSelect.vipPrice);
					this.$set(this.attr.productSelect, 'otPrice', productSelect.otPrice);
					this.$set(this, "attrValue", res);
					this.$set(this, "attrTxt", "已选择");
				} else {
					this.$set(this.attr.productSelect, "image", this.productInfo.image);
					this.$set(this.attr.productSelect, "price", this.productInfo.price);
					this.$set(this.attr.productSelect, "stock", 0);
					this.$set(this.attr.productSelect, "unique", this.productInfo.id);
					this.$set(this.attr.productSelect, "cart_num", 1);
					this.$set(this.attr.productSelect, "vipPrice", this.productInfo.vipPrice);
					this.$set(this.attr.productSelect, 'otPrice', this.productInfo.otPrice);
					this.$set(this, "attrValue", "");
					this.$set(this, "attrTxt", "请选择");
				}
			},
			ChangCoupons: function(e) {
				let coupon = e;
				let couponList = this.$util.ArrayRemove(this.couponList, 'id', coupon.id);
				this.$set(this, 'couponList', couponList);
				this.getCouponList();
			},

			setClientHeight: function() {
				let that = this;
				if (!that.good_list.length) return;
				let view = uni.createSelectorQuery().in(this).select("#list0");
				view.fields({
					size: true,
				}, data => {
					that.$set(that, 'clientHeight', data.height + 20)
				}).exec();
			},
			getGoods() {
				getProductGood().then(res => {
					let good_list = res.data.list || [];
					let count = Math.ceil(good_list.length / 6);
					let goodArray = new Array();
					for (let i = 0; i < count; i++) {
						let list = good_list.slice(i * 6, i * 6 + 6);
						if (list.length) goodArray.push({
							list: list
						});
					}
					this.$set(this, 'good_list', goodArray);
					let navList = ['商品', '评价', '详情'];
					if (goodArray.length) {
						navList.splice(2, 0, '推荐')
					}
					this.$set(this, 'navList', navList);
					this.$nextTick(() => {
						if (good_list.length) {
							// #ifndef APP-PLUS
							this.setClientHeight();
							// #endif
							// #ifdef APP-PLUS
							setTimeout(() => {
								this.setClientHeight();
							}, 1000)
							// #endif
						};
					})
				});
			},
			getGoodsDetails: function() {
				let that = this;
				getProductDetail(that.id, that.type).then(res => {
					let productInfo = res.data.productInfo;
					let arrayImg = productInfo.sliderImage;
					let sliderImage = JSON.parse(arrayImg);
					if (that.getFileType(sliderImage[0]) == 'video') {
						this.$set(this, 'videoLink', sliderImage[0]);
						sliderImage.splice(0, 1);
					}
					that.$set(that, 'sliderImage', sliderImage);
					console.log(this.sliderImage);
					that.$set(that, 'productInfo', productInfo);
					that.$set(that, 'description', productInfo.content);
					if (productInfo.industryInfo) {
						that.$set(that, 'industryInfo', productInfo.industryInfo);
					}
					that.$set(that, 'userCollect', res.data.userCollect);
					that.$set(this.attr, 'productAttr', res.data.productAttr);
					that.$set(this, 'productValue', res.data.productValue);
					for (let key in res.data.productValue) {
						let obj = res.data.productValue[key];
						that.skuArr.push(obj)
					}
					this.$set(this, "selectSku", that.skuArr[0]);
					that.$set(that.sharePacket, 'priceName', res.data.priceName);
					that.$set(that.sharePacket, 'isState', (res.data.priceName != "0" && res.data.priceName !==
						null) ? false : true);
					that.$set(that, 'activityH5', res.data.activityAllH5 ? res.data.activityAllH5 : []);
					uni.setNavigationBarTitle({
						title: productInfo.storeName.substring(0, 7) + "..."
					})

					let productAttr = this.attr.productAttr.map(item => {
						return {
							attrName: item.attrName,
							attrValues: item.attrValues.split(','),
							id: item.id,
							isDel: item.isDel,
							productId: item.productId,
							type: item.type
						}
					});
					this.$set(this.attr, 'productAttr', productAttr);

					that.getCartCount();
					//#ifdef H5
					that.make(that.uid);
					that.ShareInfo();
					this.getImageBase64(this.productInfo.image);
					// #endif
					// #ifdef MP
					that.getQrcode();
					// #endif
					setTimeout(function() {
						that.infoScroll();
					}, 500);
					// #ifdef MP
					that.imgTop = res.data.productInfo.image
					// #endif
					// #ifndef H5
					that.downloadFilestoreImage();
					// #endif
					that.DefaultSelect();
					this.showSkeleton = false
					setTimeout(() => {
						this.defaultCoupon = this.coupon.list;
					}, 1000)
				}).catch(err => {
					that.$util.Tips({
						title: err.toString()
					}, {
						tab: 3,
						url: 1
					});
					this.showSkeleton = false
				})
			},
			getProductReplyList: function() {
				getReplyProduct(this.id).then(res => {
					this.reply = res.data.productReply ? [res.data.productReply] : [];
				})
			},
			getProductReplyCount: function() {
				let that = this;
				getReplyConfig(that.id).then(res => {
					that.$set(that, 'replyChance', res.data.replyChance * 100);
					that.$set(that, 'replyCount', res.data.sumCount);
				});
			},
			infoScroll: function() {
				var that = this,
					topArr = [],
					heightArr = [];
				for (var i = 0; i < that.navList.length; i++) {
					var query = uni.createSelectorQuery().in(this);
					var idView = "#past" + i;
					query.select(idView).boundingClientRect();
					query.exec(function(res) {
						var top = res[0].top;
						var height = res[0].height;
						topArr.push(top);
						heightArr.push(height);
						that.$set(that, 'topArr', topArr);
						that.$set(that, 'heightArr', heightArr);
					});
				};
			},
			DefaultSelect: function() {
				let productAttr = this.attr.productAttr;
				let value = [];
				for (let key in this.productValue) {
					if (this.productValue[key].stock > 0) {
						value = this.attr.productAttr.length ? key.split(",") : [];
						break;
					}
				}
				for (let i = 0; i < value.length; i++) {
					this.$set(productAttr[i], "index", value[i]);
				}

				let productSelect = this.productValue[value.join(",")];
				if (productSelect && productAttr.length) {
					this.$set(this.attr.productSelect, "storeName", this.productInfo.storeName);
					this.$set(this.attr.productSelect, "image", productSelect.image);
					this.$set(this.attr.productSelect, "price", productSelect.price);
					this.$set(this.attr.productSelect, "stock", productSelect.stock);
					this.$set(this.attr.productSelect, "unique", productSelect.id);
					this.$set(this.attr.productSelect, "cart_num", 1);
					this.$set(this.attr.productSelect, "vipPrice", productSelect
						.vipPrice);
					this.$set(this.attr.productSelect, 'otPrice', productSelect.otPrice);
					this.$set(this, "attrValue", value.join(","));
					this.$set(this, "attrTxt", "已选择");
				} else if (!productSelect && productAttr.length) {
					this.$set(this.attr.productSelect, "storeName", this.productInfo.storeName);
					this.$set(this.attr.productSelect, "image", this.productInfo.image);
					this.$set(this.attr.productSelect, "price", this.productInfo.price);
					this.$set(this.attr.productSelect, "stock", 0);
					this.$set(this.attr.productSelect, "unique", this.productInfo.id);
					this.$set(this.attr.productSelect, "cart_num", 1);
					this.$set(this.attr.productSelect, "vipPrice", this.productInfo.vipPrice);
					this.$set(this.attr.productSelect, 'otPrice', this.productInfo.otPrice);
					this.$set(this, "attrValue", "");
					this.$set(this, "attrTxt", "请选择");
				} else if (!productSelect && !productAttr.length) {
					this.$set(this.attr.productSelect, "storeName", this.productInfo.storeName);
					this.$set(this.attr.productSelect, "image", this.productInfo.image);
					this.$set(this.attr.productSelect, "price", this.productInfo.price);
					this.$set(this.attr.productSelect, "stock", this.productInfo.stock);
					this.$set(this.attr.productSelect, "unique", this.productInfo.id || "");
					this.$set(this.attr.productSelect, "cart_num", 1);
					this.$set(this.attr.productSelect, "vipPrice", this.productInfo.vipPrice);
					this.$set(this.attr.productSelect, 'otPrice', this.productInfo.otPrice);
					this.$set(this, "attrValue", "");
					this.$set(this, "attrTxt", "请选择");
				}
			},
			getCouponList(type) {
				let that = this;
				if (type != undefined || type != null) {
					that.where.type = type;
				} else {
					that.where.type = "";
				}
				that.where.productId = that.id;
				if (that.loadend) return;
				if (that.loading) return;
				that.loading = true;
				that.loadTitle = '';
				getCoupons(that.where).then(res => {
					let list = res.data.list;
					let couponList = that.$util.SplitArray(list, that.coupon.list);
					let loadend = list.length < that.where.limit;
					that.loadend = loadend;
					that.loading = false;
					that.loadTitle = loadend ? '已全部加载' : '加载更多';
					that.$set(that.coupon, 'list', couponList);
					that.$set(that.where, 'page', that.where.page + 1);
				});
			},
			async getCouponType() {
				let dataList = await getCoupons({
					productId: this.id
				});
				if (dataList.length) {
					this.couponDeaultType = dataList.data;
					this.$set(this.coupon, 'type', dataList);
				}

			},
			tabCouponType(type) {
				this.loadend = false;
				this.loading = false;
				this.where.page = 1;
				this.where.limit = 999;
				this.$set(this.coupon, 'list', []);
				this.$set(this.coupon, 'type', type);
				this.couponType = type;
				this.getCouponList(type);
			},

			ChangCouponsUseState(index) {
				let that = this;
				that.coupon.list[index].isUse = true;
				this.$set(that.coupon, 'list', that.coupon.list);
				this.$set(that.coupon, 'coupon', false);
			},
			setCollect: function() {
				let that = this;
				if (this.isLogin === false) {
					toLogin();
				} else {
					if (this.userCollect) {
						collectDel(this.productInfo.id).then(res => {
							that.$set(that, 'userCollect', !that.userCollect);
						})
					} else {
						collectAdd(this.productInfo.id).then(res => {
							that.$set(that, 'userCollect', !that.userCollect);
						})
					}
				}
			},
			selecAttr: function() {
				this.$set(this.attr, 'cartAttr', true);
				this.$set(this, 'isOpen', true);
			},
			couponTap: function() {
				let that = this;
				if (that.isLogin === false) {
					toLogin();
				} else {
					that.$set(that.coupon, 'coupon', true);
				}
			},
			onMyEvent: function() {
				this.$set(this.attr, 'cartAttr', false);
				this.$set(this, 'isOpen', false);
			},
			joinCart: function(e) {
				if (this.isLogin === false) {
					toLogin();
				} else {
					this.goCat(1);
				}
			},
			goCat: function(num) {
				let that = this,
					productSelect = that.productValue[this.attrValue];
				if (that.attrValue) {
					that.attr.cartAttr = !that.isOpen ? true : false;
				} else {
					if (that.isOpen) that.attr.cartAttr = true;
					else that.attr.cartAttr = !that.attr.cartAttr;
				}
				if (that.attr.cartAttr === true && that.isOpen === false)
					return (that.isOpen = true);
				if (
					that.attr.productAttr.length &&
					productSelect.stock === 0 &&
					that.isOpen === true
				)
					return that.$util.Tips({
						title: "产品库存不足，请选择其它"
					});
				if (num === 1) {
					let q = {
						productId: parseFloat(that.id),
						cartNum: parseFloat(that.attr.productSelect.cart_num),
						isNew: false,
						productAttrUnique: that.attr.productSelect !== undefined ?
							that.attr.productSelect.unique : that.productInfo.id
					};
					postCartAdd(q).then(function(res) {
							that.isOpen = false;
							that.attr.cartAttr = false;
							that.$util.Tips({
								title: "添加购物车成功",
								success: () => {
									that.getCartCount(true);
								}
							});
						})
						.catch(res => {
							that.isOpen = false;
							return that.$util.Tips({
								title: res
							});
						});
				} else {
					this.getPreOrder();
				}
			},
			getCartCount: function(isAnima) {
				let that = this;
				const isLogin = that.isLogin;
				if (isLogin) {
					getCartCounts(true, 'total').then(res => {
						that.CartCount = res.data.count;
						if (isAnima) {
							that.animated = true;
							setTimeout(function() {
								that.animated = false;
							}, 500);
						}
					});
				}
			},
			goBuy: Debounce(function(e) {
				if (this.isLogin === false) {
					toLogin();
				} else {
					this.goCat(0);
				}
			}),
			getPreOrder: function() {
				this.$Order.getPreOrder(this.type === 'normal' ? 'buyNow' : 'video', [{
					"attrValueId": parseFloat(this.attr.productSelect.unique),
					"productId": parseFloat(this.id),
					"productNum": parseFloat(this.attr.productSelect.cart_num)
				}]);
				this.isOpen = false;
			},
			authColse: function(e) {
				this.isShowAuth = e
			},
			listenerActionSheet: function() {
				// #ifdef H5
				if (this.$wechat.isWeixin() === true) {
					this.weixinStatus = true;
				}
				// #endif
				this.goPoster()
				this.posters = true;
			},
			closePosters: function() {
				this.posters = false;
				this.currentPage = false;
			},
			posterImageClose: function() {
				this.canvasStatus = false
				this.posters = false;
			},
			setDomain: function(url) {
				url = url ? url.toString() : '';
				if (url.indexOf("https://") > -1) return url;
				else return url.replace('http://', 'https://');
			},
			downloadFilestoreImage: function() {
				let that = this;
				uni.downloadFile({
					url: that.setDomain(that.productInfo.image),
					success: function(res) {
						that.storeImage = res.tempFilePath;
					},
					fail: function() {
						return that.$util.Tips({
							title: ''
						});
						that.storeImage = '';
					},
				});
			},
			goFriend: function() {
				this.posters = false;
			},
			getQrcode() {
				let that = this;
				let data = {
					pid: that.uid,
					id: that.id,
					path: 'pages/goods/goods_details/index'
				}
				getQrcode(data).then(res => {
					base64src(res.data.code, Date.now(), res => {
						that.PromotionCode = res;
					});

				}).catch(err => {
					that.errT = err;
				});
			},
			make(uid) {
				let href = location.href.split('?')[0] + "?id=" + this.id + "&spread=" + this.uid;
				uQRCode.make({
					canvasId: 'qrcode',
					text: href,
					size: this.qrcodeSize,
					margin: 10,
					success: res => {
						this.PromotionCode = res;
					},
					complete: () => {},
					fail: res => {
						this.$util.Tips({
							title: '海报二维码生成失败！'
						});
					}
				})
			},
			getImageBase64: function(images) {
				let that = this;
				imageBase64({
					url: images
				}).then(res => {
					that.imgTop = res.data.code;
				})
			},
			goPoster: function() {
				let that = this;
				uni.showLoading({
					title: '海报生成中',
					mask: true
				});
				that.posters = false;
				let arrImagesUrl = '';
				let arrImagesUrlTop = '';
				if (!that.PromotionCode) {
					uni.hideLoading();
					that.$util.Tips({
						title: that.errT
					});
					return
				}
				setTimeout(() => {
					if (!that.imgTop) {
						uni.hideLoading();
						that.$util.Tips({
							title: '无法生成商品海报！'
						});
						return
					}
				}, 1000);
				uni.downloadFile({
					url: that.imgTop,
					success: (res) => {
						arrImagesUrlTop = res.tempFilePath;
						let arrImages = [that.posterbackgd, arrImagesUrlTop, that.PromotionCode];
						let storeName = that.productInfo.storeName;
						let price = that.productInfo.price;
						setTimeout(() => {
							that.$util.PosterCanvas(arrImages, storeName, price, that.productInfo
								.otPrice,
								function(tempFilePath) {
									that.imagePath = tempFilePath;
									that.canvasStatus = true;
									uni.hideLoading();
								});
						}, 500);
					}
				});
			},
			getpreviewImage: function() {
				if (this.imagePath) {
					let photoList = [];
					photoList.push(this.imagePath)
					uni.previewImage({
						urls: photoList,
						current: this.imagePath
					});
				} else {
					this.$util.Tips({
						title: '您的海报尚未生成'
					});
				}
			},
			// #ifdef MP
			savePosterPath: function() {
				let that = this;
				uni.getSetting({
					success(res) {
						if (!res.authSetting['scope.writePhotosAlbum']) {
							uni.authorize({
								scope: 'scope.writePhotosAlbum',
								success() {
									uni.saveImageToPhotosAlbum({
										filePath: that.imagePath,
										success: function(res) {
											that.posterImageClose();
											that.$util.Tips({
												title: '保存成功',
												icon: 'success'
											});
										},
										fail: function(res) {
											that.$util.Tips({
												title: '保存失败'
											});
										}
									})
								}
							})
						} else {
							uni.saveImageToPhotosAlbum({
								filePath: that.imagePath,
								success: function(res) {
									that.posterImageClose();
									that.$util.Tips({
										title: '保存成功',
										icon: 'success'
									});
								},
								fail: function(res) {
									that.$util.Tips({
										title: '保存失败'
									});
								},
							})
						}
					}
				})
			},
			// #endif
			ShareInfo() {
				let data = this.productInfo;
				let href = location.href;
				if (this.$wechat.isWeixin()) {
					href = href.indexOf("?") === -1 ? href + "?spread=" + this.uid : href + "&spread=" + this.uid;
					let configAppMessage = {
						desc: app.globalData.companyName,
						title: data.storeName,
						link: href,
						imgUrl: data.image
					};
					this.$wechat.wechatEvevt([
						"updateAppMessageShareData",
						"updateTimelineShareData",
						"onMenuShareAppMessage",
						"onMenuShareTimeline"
					], configAppMessage).then(res => {
					}).catch(err => {
						console.log(err);
					})
				}
			},
			showShare(status) {
				let that = this;
				that.$set(that.sharePacket, 'touchstart', status);
			},
			hideNav() {
				this.currentPage = false;
			},
			linkPage(url) {
				if (url == '/pages/index/index' || url == '/pages/order_addcart/order_addcart' || url ==
					'/pages/user/index') {
					uni.switchTab({
						url
					})
				} else {
					uni.navigateTo({
						url
					})
				}
				this.currentPage = false
			},
			showImg(index) {
				this.$refs.cusPreviewImg.open(this.selectSku.suk)
			},
			changeSwitch(e) {
				let productSelect = this.skuArr[e];
				this.$set(this, 'selectSku', productSelect);
				var skuList = productSelect.suk.split(',');
				skuList.forEach((i, index) => {
					this.$set(this.attr.productAttr[index], 'index', skuList[index]);
				})
				if (productSelect) {
					this.$set(this.attr.productSelect, "image", productSelect.image);
					this.$set(this.attr.productSelect, "price", productSelect.price);
					this.$set(this.attr.productSelect, "stock", productSelect.stock);
					this.$set(this.attr.productSelect, "unique", productSelect.id);
					this.$set(this.attr.productSelect, "vipPrice", productSelect.vipPrice);
					this.$set(this, 'attrTxt', "已选择");
					this.$set(this, 'attrValue', productSelect.suk)
				}
			},
			getFileType(fileName) {
				let suffix = '';
				let result = '';
				try {
					const flieArr = fileName.split('.');
					suffix = flieArr[flieArr.length - 1];
				} catch (err) {
					suffix = '';
				}
				if (!suffix) {
					return false;
				}
				suffix = suffix.toLocaleLowerCase();
				const imglist = ['png', 'jpg', 'jpeg', 'bmp', 'gif'];
				result = imglist.find(item => item === suffix);
				if (result) {
					return 'image';
				}
				const videolist = ['mp4', 'm2v', 'mkv', 'rmvb', 'wmv', 'avi', 'flv', 'mov', 'm4v'];
				result = videolist.find(item => item === suffix);
				if (result) {
					return 'video';
				}
				return 'other';
			},
			videoPause() {

			}
		},
	}
</script>

<style scoped lang="scss">
/* ===== 新配色变量 ===== */
$primary: #0061a5;
$primary-light: rgba(0, 97, 165, 0.08);
$primary-border: rgba(0, 97, 165, 0.18);
$surface: #f9f9fc;
$surface-card: #ffffff;
$on-surface: #1a1c1e;
$on-surface-variant: #3f4753;
$outline-variant: #dde0e8;
$surface-variant: #e8ebf2;
$surface-container-low: #f3f4f8;
$secondary: #5c5f60;
$tertiary: #895000;
$error: #ba1a1a;
$red-gradient: linear-gradient(90deg, rgba(246, 122, 56, 1) 0%, rgba(241, 27, 9, 1) 100%);

$r-sm: 8rpx;
$r-md: 14rpx;
$r-lg: 20rpx;
$r-xl: 28rpx;

$ease: cubic-bezier(0.22, 1, 0.36, 1);

/* ===== 动画 ===== */
@keyframes slideUp {
	from { opacity: 0; transform: translateY(30rpx); }
	to { opacity: 1; transform: translateY(0); }
}
@keyframes fadeIn {
	from { opacity: 0; }
	to { opacity: 1; }
}
@keyframes dropIn {
	from { opacity: 0; transform: translateY(-20rpx) scale(0.96); }
	to { opacity: 1; transform: translateY(0) scale(1); }
}
@keyframes elasticIn {
	0% { opacity: 0; transform: scale(0.85); }
	60% { transform: scale(1.03); }
	100% { opacity: 1; transform: scale(1); }
}

/* ===== 基础布局 ===== */
.product-con {
	height: 100%;
	display: flex;
	flex-direction: column;
	background: $surface;
}
.detail_container {
	flex: 1;
	overflow: hidden;
}

/* ===== margin/padding 工具类 ===== */
.mb30 { margin-bottom: 24rpx; }
.borRadius14 { border-radius: $r-md; }
.pl-20 { padding-left: 20rpx; }
.px-20 { padding: 0 20rpx 0; }
.px-12 { padding-left: 12rpx; padding-right: 12rpx; }
.pl-2 { padding-left: 20rpx; }
.font_color { color: $primary; }
.flex { display: flex; }
.justify-center { justify-content: center; }
.align-center { align-items: center; }
.align-baseline { align-items: baseline; }
.bg_color { background: $primary; color: #fff; }
.pad30 { padding: 0 24rpx; }

/* ===== Hero 产品图 ===== */
.hero-section {
	position: relative;
	width: 100%;
	aspect-ratio: 1 / 1;
	overflow: hidden;
}
.hero-swiper {
	width: 100%;
	height: 100%;
}
.hero-image {
	width: 100%;
	height: 100%;
	display: block;
}
.hero-tags {
	position: absolute;
	bottom: 28rpx;
	left: 24rpx;
	display: flex;
	gap: 12rpx;
	z-index: 2;
}
.hero-tag {
	padding: 6rpx 20rpx;
	font-size: 22rpx;
	color: #fff;
	background: rgba(0, 0, 0, 0.45);
	backdrop-filter: blur(8px);
	border-radius: 200rpx;
	border: 1rpx solid rgba(255, 255, 255, 0.2);
	letter-spacing: 1rpx;
}

/* ===== 产品信息卡片 ===== */
.product-info-card {
	animation: slideUp 400ms $ease both;
	background: $surface-card;
	border-radius: $r-md;
	padding: 28rpx 24rpx;
	border: 1rpx solid $outline-variant;
}
.info-header {
	position: relative;
}
.info-price-area {
	display: flex;
	align-items: baseline;
	flex-wrap: wrap;
}
.price-symbol {
	font-size: 28rpx;
	font-weight: 700;
	color: $tertiary;
}
.price-value {
	font-size: 44rpx;
	font-weight: 700;
	color: $tertiary;
}
.vip-row {
	display: flex;
	align-items: center;
	margin-left: 8rpx;
}
.vip_icon {
	width: 44rpx;
	height: 28rpx;
}
.vip_money {
	background: linear-gradient(135deg, #FFE7B9, #FFD98C);
	border-radius: $r-sm;
	font-size: 22rpx;
	color: $on-surface;
	line-height: 28rpx;
	text-align: center;
	padding: 0 6rpx;
	margin-left: -4rpx;
}
.share-icon-btn {
	position: absolute;
	right: -8rpx;
	top: 0;
	font-size: 36rpx;
	color: $on-surface-variant;
}
.info-title {
	font-size: 30rpx;
	font-weight: 600;
	color: $on-surface;
	margin-top: 16rpx;
	line-height: 1.4;
}
.info-desc {
	font-size: 26rpx;
	color: $on-surface-variant;
	margin-top: 10rpx;
	line-height: 1.5;
}
.info-meta {
	margin-top: 16rpx;
	font-size: 24rpx;
	color: $on-surface-variant;
}
.info-meta > view {
	flex: 1;
	&:nth-child(2) { text-align: center; }
	&:nth-child(3) { text-align: right; }
}

/* 优惠券 / 活动 */
.coupon {
	margin-top: 16rpx;
	padding-top: 16rpx;
	border-top: 1rpx solid $surface-variant;
	font-size: 26rpx;
	.iconfont { color: $on-surface-variant; font-size: 24rpx; }
}
.activity {
	padding: 0 20rpx;
	background: $primary-light;
	border: 1rpx solid $primary-border;
	color: $primary;
	font-size: 24rpx;
	line-height: 34rpx;
	margin-left: 4rpx;
	border-radius: $r-sm;
	position: relative;
	&:before {
		content: ' ';
		position: absolute;
		width: 7rpx; height: 10rpx;
		border-radius: 0 7rpx 7rpx 0;
		border-color: $primary-border;
		background-color: $surface-card;
		bottom: 50%; left: -3rpx;
		margin-bottom: -5rpx;
	}
	&:after {
		content: ' ';
		position: absolute;
		width: 7rpx; height: 10rpx;
		border-radius: 7rpx 0 0 7rpx;
		border-color: $primary-border;
		background-color: $surface-card;
		right: -3rpx; bottom: 50%;
		margin-bottom: -5rpx;
	}
}
.activityName { line-height: 44rpx; }
.activityBox { }
.line1 { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }

/* ===== 活动标签 ===== */
.activity_pin, .activity_miao, .activity_kan {
	width: auto;
	height: 44rpx;
	line-height: 44rpx;
	padding: 0 15rpx;
	background: $primary;
	border-radius: 22rpx;
	color: #fff;
}
.activity_miao, .activity_kan { margin-left: 19rpx; }
.iconfonts { color: #fff !important; font-size: 28rpx; }
.activity_title { font-size: 24rpx; color: #fff; }

/* ===== 规格选择器 Bento Card ===== */
.spec-selector {
	animation: slideUp 400ms $ease both;
	animation-delay: 60ms;
	background: $surface-card;
	border-radius: $r-md;
	padding: 28rpx 24rpx;
	border: 1rpx solid $outline-variant;
}
.spec-title-row {
	margin-bottom: 24rpx;
	padding-bottom: 20rpx;
	border-bottom: 1rpx solid $surface-variant;
}
.spec-title-bar {
	width: 4rpx;
	height: 32rpx;
	background: $primary;
	border-radius: 2rpx;
	margin-right: 16rpx;
}
.spec-title-text {
	font-size: 30rpx;
	font-weight: 600;
	color: $on-surface;
}
.spec-group {
	margin-bottom: 22rpx;
	&:last-of-type { margin-bottom: 0; }
}
.spec-label {
	font-size: 26rpx;
	font-weight: 500;
	color: $on-surface;
	margin-bottom: 14rpx;
}
.spec-options {
	display: flex;
	flex-wrap: wrap;
	gap: 14rpx;
}
.spec-options.spec-grid-4 {
	.spec-option {
		width: calc((100% - 42rpx) / 4);
		text-align: center;
		justify-content: center;
		padding: 14rpx 8rpx;
	}
}
.spec-options.spec-row {
	.spec-option {
		padding: 14rpx 28rpx;
	}
}
.spec-option {
	display: flex;
	align-items: center;
	font-size: 26rpx;
	color: $on-surface-variant;
	background: $surface-container-low;
	border-radius: $r-sm;
	border: 1rpx solid transparent;
	transition: all 0.2s $ease;
	&.active {
		color: $primary;
		background: $primary-light;
		border-color: $primary-border;
		font-weight: 500;
	}
}
.spec-sku-row {
	margin-top: 20rpx;
	padding-top: 20rpx;
	border-top: 1rpx solid $surface-variant;
}
.attrImg {
	width: 66rpx;
	height: 66rpx;
	border-radius: $r-sm;
	display: block;
	margin-right: 14rpx;
}
.switchTxt {
	height: 60rpx;
	line-height: 60rpx;
	background: $surface-container-low;
	padding: 0 24rpx;
	border-radius: $r-sm;
	text-align: center;
	color: $secondary;
	font-size: 24rpx;
}
.theme_price { color: $tertiary; }

/* ===== 用户评价 ===== */
.userEvaluation {
	animation: slideUp 400ms $ease both;
	animation-delay: 100ms;
	background: $surface-card;
	border-radius: $r-md;
	padding: 28rpx 24rpx;
	margin-top: 24rpx;
	border: 1rpx solid $outline-variant;
	.title {
		font-size: 28rpx;
		font-weight: 500;
		color: $on-surface;
		i { font-style: normal; color: $on-surface-variant; font-size: 24rpx; }
	}
	i { display: inline-block; }
}
.praise { color: $primary; }

/* ===== 技术参数表格 ===== */
.tech-table-section {
	animation: slideUp 400ms $ease both;
	animation-delay: 80ms;
	background: $surface-card;
	border-radius: $r-md;
	padding: 28rpx 24rpx;
	margin-top: 24rpx;
	border: 1rpx solid $outline-variant;
}
.tech-title-row {
	margin-bottom: 24rpx;
	padding-bottom: 20rpx;
	border-bottom: 1rpx solid $surface-variant;
}
.tech-title-bar {
	width: 4rpx;
	height: 32rpx;
	background: $primary;
	border-radius: 2rpx;
	margin-right: 16rpx;
}
.tech-title-text {
	font-size: 30rpx;
	font-weight: 600;
	color: $on-surface;
}
.tech-table {
	border-radius: $r-md;
	overflow: hidden;
	border: 1rpx solid $outline-variant;
}
.tech-row {
	display: flex;
	align-items: center;
	min-height: 76rpx;
	border-bottom: 1rpx solid $surface-variant;
	&:last-child { border-bottom: none; }
}
.tech-label {
	width: 33%;
	flex-shrink: 0;
	padding: 18rpx 20rpx;
	font-size: 26rpx;
	color: $on-surface-variant;
	background: $surface-container-low;
	border-right: 1rpx solid $surface-variant;
	font-weight: 500;
}
.tech-value {
	width: 67%;
	flex-shrink: 0;
	padding: 18rpx 20rpx;
	font-size: 26rpx;
	color: $on-surface;
	font-weight: 500;
	background: $surface-card;
}
.tech-tags {
	margin-top: 24rpx;
	flex-wrap: wrap;
}
.tech-tag {
	display: inline-flex;
	align-items: center;
	padding: 8rpx 22rpx;
	margin-right: 16rpx;
	margin-bottom: 12rpx;
	font-size: 22rpx;
	color: $primary;
	background: $primary-light;
	border-radius: $r-sm;
	border: 1rpx solid $primary-border;
	transition: all 0.2s $ease;
}

/* ===== 产品结构详解 ===== */
.structure-section {
	animation: slideUp 400ms $ease both;
	animation-delay: 120ms;
	background: $surface-card;
	border-radius: $r-md;
	padding: 28rpx 24rpx;
	margin-top: 24rpx;
	border: 1rpx solid $outline-variant;
}
.structure-body {
	display: flex;
	align-items: flex-start;
	gap: 24rpx;
}
.structure-list {
	flex: 1;
	display: flex;
	flex-direction: column;
	gap: 20rpx;
}
.structure-item {
	display: flex;
	align-items: flex-start;
	gap: 16rpx;
}
.structure-num {
	flex-shrink: 0;
	width: 56rpx;
	height: 56rpx;
	line-height: 56rpx;
	text-align: center;
	font-size: 22rpx;
	font-weight: 700;
	color: $primary;
	background: $primary-light;
	border-radius: $r-sm;
	border: 1rpx solid $primary-border;
}
.structure-text {
	flex: 1;
	display: flex;
	flex-direction: column;
	gap: 4rpx;
	padding-top: 4rpx;
}
.structure-item-label {
	font-size: 28rpx;
	font-weight: 600;
	color: $on-surface;
}
.structure-item-desc {
	font-size: 24rpx;
	color: $on-surface-variant;
	line-height: 1.5;
}
.structure-diagram {
	flex-shrink: 0;
	width: 200rpx;
	height: 260rpx;
	border-radius: $r-md;
	overflow: hidden;
	border: 1rpx solid $outline-variant;
}
.structure-img {
	width: 100%;
	height: 100%;
	display: block;
}

/* ===== 产品详情 ===== */
.product-intro {
	animation: slideUp 400ms $ease both;
	animation-delay: 160ms;
	background: $surface-card;
	border-radius: $r-md;
	padding: 28rpx 24rpx;
	margin: 24rpx 24rpx 24rpx 24rpx;
	border: 1rpx solid $outline-variant;
	.title {
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 16rpx;
		margin-bottom: 24rpx;
		image { width: 24rpx; height: 20rpx; }
		.sp {
			font-size: 30rpx;
			font-weight: 700;
			color: $on-surface;
			&::before {
				content: '';
				display: inline-block;
				width: 4rpx;
				height: 32rpx;
				background: $primary;
				border-radius: 2rpx;
				margin-right: 12rpx;
				vertical-align: middle;
			}
		}
	}
	.conter { line-height: 1.8; }
}

/* ===== 优品推荐 ===== */
.superior {
	background: $surface-card;
	margin-top: 24rpx;
	padding: 0 24rpx 30rpx 24rpx;
	border-radius: $r-md;
	animation: slideUp 400ms $ease both;
	animation-delay: 180ms;
	border: 1rpx solid $outline-variant;
	.title {
		height: 98rpx;
		image { width: 20rpx; height: 20rpx; }
		.titleTxt { margin: 0 10rpx; font-size: 30rpx; color: $on-surface; font-weight: 500; }
	}
	.slider-banner { width: 100%; margin: 0 auto; position: relative; }
	.slider-banner swiper { height: 100%; width: 100%; }
	.slider-banner swiper-item { height: 100%; }
	.slider-banner .list { width: 100%; }
	.slider-banner .list .item {
		width: 198rpx; margin: 0 22rpx 30rpx 0; font-size: 26rpx;
		&:nth-of-type(3n) { margin-right: 0; }
		.pictrue { position: relative; width: 100%; height: 198rpx; }
		.pictrue image { width: 100%; height: 100%; border-radius: $r-sm; }
		.name { color: $on-surface; margin-top: 12rpx; }
	}
}

/* ===== 底部操作栏 ===== */
.footer {
	padding: 0 20rpx 0 24rpx;
	position: fixed;
	bottom: 0;
	width: 100%;
	box-sizing: border-box;
	background: $surface-card;
	z-index: 277;
	border-top: 1rpx solid $outline-variant;
	height: 100rpx;
	height: calc(100rpx + constant(safe-area-inset-bottom));
	height: calc(100rpx + env(safe-area-inset-bottom));
	box-shadow: 0 -2rpx 16rpx rgba(0, 0, 0, 0.04);
}
.footer-left {
	gap: 8rpx;
}
.footer-icon-btn {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	width: 90rpx;
	font-size: 18rpx;
	color: $on-surface-variant;
	padding: 0;
	margin: 0;
	line-height: normal;
	background: transparent;
	border: none;
	&::after { border: none; }
	.iconfont {
		font-size: 36rpx;
		margin-bottom: 2rpx;
		&.icon-shoucang1 { color: $primary; }
	}
}
.footer-right {
	gap: 16rpx;
	align-items: center;
}
.btn-outline {
	display: flex;
	align-items: center;
	justify-content: center;
	height: 72rpx;
	padding: 0 32rpx;
	font-size: 28rpx;
	font-weight: 500;
	color: $primary;
	background: transparent;
	border: 2rpx solid $primary;
	border-radius: $r-lg;
	letter-spacing: 1rpx;
	transition: all 0.2s $ease;
	white-space: nowrap;
	&:active {
		background: $primary-light;
	}
}
.btn-primary {
	display: flex;
	align-items: center;
	justify-content: center;
	height: 72rpx;
	padding: 0 32rpx;
	font-size: 28rpx;
	font-weight: 500;
	color: #fff;
	background: $primary;
	border-radius: $r-lg;
	letter-spacing: 1rpx;
	transition: all 0.2s $ease;
	white-space: nowrap;
	&:active {
		background: darken($primary, 8%);
	}
}
.btn-disabled {
	display: flex;
	align-items: center;
	justify-content: center;
	height: 72rpx;
	padding: 0 32rpx;
	font-size: 28rpx;
	font-weight: 500;
	color: #fff;
	background: #bbb;
	border-radius: $r-lg;
	letter-spacing: 1rpx;
	white-space: nowrap;
}

/* ===== 导航栏 (保留原样) ===== */
.navbar {
	position: fixed; top: 0; left: 0; z-index: 99; width: 100%;
}
.navbar .navbarH { position: relative; }
.navbar .navbarH .navbarCon { position: absolute; bottom: 0; height: 100rpx; width: 100%; }
.bgwhite { background: $surface-card; }
.select_nav {
	width: 170rpx !important; height: 60rpx !important;
	border-radius: 33rpx;
	background: rgba(255, 255, 255, 0.45);
	backdrop-filter: blur(12px);
	border: 1px solid rgba(0,0,0,0.06);
	color: $on-surface;
	position: fixed;
	font-size: 18px;
	line-height: 58rpx;
	z-index: 1000;
	left: 14rpx;
	animation: fadeIn 300ms $ease both;
}
.nav_line {
	content: '';
	display: inline-block;
	width: 1px; height: 34rpx;
	background: $outline-variant;
	position: absolute;
	left: 0; right: 0;
	margin: auto;
}
.h5_back {
	color: $on-surface;
	position: fixed;
	left: 20rpx;
	font-size: 32rpx;
	text-align: center;
	width: 58rpx; height: 58rpx;
	background: rgba(255, 255, 255, 0.45);
	backdrop-filter: blur(12px);
	border: 1px solid rgba(0, 0, 0, 0.06);
	border-radius: 50%;
}
.icon-xiangzuo {
	/* #ifdef H5 */
	top: 20rpx !important;
	/* #endif */
}
.right_select {
	width: 58rpx; height: 58rpx;
	background: rgba(255, 255, 255, 0.45);
	backdrop-filter: blur(12px);
	border: 1px solid rgba(0, 0, 0, 0.06);
	border-radius: 50%;
	position: fixed; right: 20rpx;
	text-align: center;
	line-height: 58rpx;
	animation: fadeIn 300ms $ease both;
}
.tab_nav {
	width: 100%; height: 96rpx; padding: 0 30rpx 0;
}
.tab_nav .header {
	width: 100%; height: 96rpx;
	padding: 20rpx 80rpx 0;
	font-size: 30rpx;
	color: $on-surface;
	background: $surface-card;
}
.navbar .header .item { position: relative; margin: 0 25rpx; }
.navbar .header .item.on:before {
	position: absolute;
	width: 60rpx; height: 5rpx;
	content: "";
	background: $primary;
	bottom: -10rpx;
	left: 50%;
	margin-left: -28rpx;
	border-radius: 3rpx;
}

/* ===== 导航弹出菜单 ===== */
.dialog_nav {
	position: absolute;
	/* #ifdef MP */
	left: 14rpx;
	/* #endif */
	/* #ifdef H5 || APP-PLUS */
	right: 14rpx;
	/* #endif */
	width: 240rpx;
	background: $surface-card;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
	z-index: 310;
	border-radius: $r-md;
	animation: dropIn 350ms $ease both;
	border: 1rpx solid $outline-variant;
	&::before {
		content: '';
		width: 0; height: 0;
		position: absolute;
		/* #ifdef MP */
		left: 0; right: 0; margin: auto;
		/* #endif */
		/* #ifdef H5 || APP-PLUS */
		right: 8px;
		/* #endif */
		top: -9px;
		border-bottom: 10px solid $surface-card;
		border-left: 10px solid transparent;
		border-right: 10px solid transparent;
	}
}
.dialog_nav_item {
	width: 100%; height: 84rpx; line-height: 84rpx;
	padding: 0 20rpx 0;
	box-sizing: border-box;
	border-bottom: 1rpx solid $surface-variant;
	font-size: 28rpx; color: $on-surface;
	position: relative;
	transition: background 0.2s $ease;
	&:active { background: $surface-container-low; }
	.iconfont { font-size: 32rpx; }
}
.dialog_after {
	::after {
		content: '';
		position: absolute; width: 172rpx; height: 1px;
		background-color: $surface-variant;
		bottom: 0; right: 0;
	}
}

/* ===== 遮罩 ===== */
.mask {
	z-index: 300 !important;
	position: fixed; top: 0; left: 0; right: 0; bottom: 0;
	background: rgba(0, 0, 0, 0.5);
	animation: fadeIn 250ms $ease both;
}
.mask_transparent {
	position: fixed; top: 0; left: 0; right: 0; bottom: 0;
	background: transparent;
	z-index: 300;
}

/* ===== 分享面板 ===== */
.generate-posters {
	width: 100%; height: 318rpx;
	background: $surface-card;
	position: fixed; left: 0; bottom: 0;
	z-index: 388;
	transform: translate3d(0, 100%, 0);
	transition: all 0.4s $ease;
	border-top: 1rpx solid $outline-variant;
	border-radius: $r-lg $r-lg 0 0;
	box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.05);
	.generateCon { height: 220rpx; }
	.generateClose { height: 98rpx; font-size: 28rpx; color: $secondary; border-top: 1px solid $surface-variant; }
	.item {
		.pictrue { width: 96rpx; height: 96rpx; border-radius: 50%; margin: 0 auto 6rpx auto;
			image { width: 100%; height: 100%; border-radius: 50%; }
		}
	}
}
.generate-posters.on { transform: translate3d(0, 0, 0); }
.generate-posters .item { flex: 1; text-align: center; font-size: 30rpx; }
.generate-posters .item .iconfont { font-size: 80rpx; color: #5eae72; }
.generate-posters .item .iconfont.icon-haibao { color: $primary; }
.generate-posters .item .iconfont.icon-haowuquan1 { color: $tertiary; }

/* ===== 海报 ===== */
.poster-pop {
	position: fixed;
	width: 450rpx; height: 714rpx;
	top: 50%; left: 50%;
	transform: translateX(-50%);
	margin-top: -432rpx;
	z-index: 399;
	animation: elasticIn 500ms $ease both;
	image { width: 100%; height: 100%; display: block; border-radius: $r-md; }
}
.canvas { position: fixed; z-index: -5; opacity: 0; }

/* ===== 分享引导 ===== */
.share-box {
	z-index: 1000; position: fixed;
	left: 0; top: 0; width: 100%; height: 100%;
	animation: fadeIn 300ms $ease both;
	image { width: 100%; height: 100%; }
}

/* ===== 按钮重置 ===== */
button {
	padding: 0; margin: 0; line-height: normal; background: $surface-card;
	&::after { border: 0; }
}

/* ===== 标签角标 ===== */
.pictrue_log {
	width: 80upx; height: 40upx;
	border-radius: 10upx 0 12upx 0;
	line-height: 40upx;
	font-size: 24upx;
}
.pictrue_log_class {
	z-index: 3;
	background: $red-gradient;
	position: absolute;
	top: 0; left: 0;
	color: #fff;
	text-align: center;
}

/* ===== 底部留白 ===== */
.bottom-spacer {
	height: 300rpx;
	height: calc(280rpx + env(safe-area-inset-bottom));
}
</style>
