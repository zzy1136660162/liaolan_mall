<template>
	<view class="page" :data-theme="theme" :style="{height:winHeight + 'px'}">
		<stitchCate v-show="currentPage == 'one'"></stitchCate>
		<tab-bar v-if="showTabBar"></tab-bar>
	</view>
</template>
<script>
	import tabBar from '@/components/tab-bar/index.vue';
	import stitchCate from './components/stitch_cate';
	import {getShare} from '@/api/public.js';
	import {mapGetters} from 'vuex';
	const app = getApp();
	export default {
		data() {
			return {
				showTabBar:true,
				currentPage:'one',
				theme:app.globalData.theme,
				showSlide:true,
				winHeight:'',
				configApi: {},
			}
		},
		computed: mapGetters(['isLogin', 'uid']),
		onLoad(){
			let that = this;
			let config = that.$Cache.getItem('categoryConfig');
			that.showSlide = config.isShowCategory == 'true'? true : false;
			switch (config.categoryConfig) {
				case '1':
					that.$set(that,'currentPage','one');
					break;
				case '2':
					that.$set(that,'currentPage','two');
					break;
				case '3':
					that.$set(that,'currentPage','three');
					this.showTabBar=false
					break;
				case '4':
					that.$set(that,'currentPage','four');
					this.showTabBar=false
					break;
			}
			uni.getSystemInfo({
			    success: function (res) {
			        that.winHeight = res.windowHeight;
			    }
			});
			// #ifdef H5
			that.shareApi();
			// #endif
		},
		onShow(){
			switch (this.currentPage){
				case 'one':
					break;
				case 'two':
					break;
				case 'three':
					this.showTabBar=false
					setTimeout(()=>{
						if(this.isLogin){
							this.$refs.classThree.getCartNum();
							this.$refs.classThree.getCartLists(1);
						}
					},500)
					break;
				case 'four':
					this.showTabBar=false
					setTimeout(()=>{
						if(this.isLogin){
							this.$refs.classFour.getCartNum();
							this.$refs.classFour.getCartLists(1);
						}
					},500)
					break;
			}
		},
		components:{
			stitchCate,tabBar
		},
		methods:{
			shareApi: function() {
				getShare().then(res => {
					this.$set(this, 'configApi', res.data);
					// #ifdef H5
					this.setOpenShare(res.data);
					// #endif
				})
			},
			setOpenShare: function(data) {
				let that = this;
				if (that.$wechat.isWeixin()) {
					let configAppMessage = {
						desc: data.synopsis,
						title: data.title,
						link: location.href,
						imgUrl: data.img
					};
					that.$wechat.wechatEvevt(["updateAppMessageShareData", "updateTimelineShareData"],
						configAppMessage);
				}
			},
		},
		onReachBottom(){
			if(this.currentPage=='two'){
				this.$refs.classTwo.getProductList();
			}
			if(this.currentPage=='three'){
				this.$refs.classThree.productslist();
			}
			if(this.currentPage=='four'){
				this.$refs.classFour.productslist();
			}
		}
	}
</script>
<style lang="scss">
	.page{
		background: #fff;
		height: 100% !important;
	}
</style>