<template>
	<view :data-theme="theme">
		<view class='wrapper'>
			<view class='item borRadius14'>
				<view class="title">￥<text>{{payPrice}}</text></view>
				<view class='list'>
					<block v-for="(item,index) in cartArr" :key='index'>
						<view v-if="item.payStatus === 1" class='payItem acea-row row-middle'
							:class='active==index ?"on":""' @tap='payItem(index,item)'>
							<view class='name acea-row row-center-wrapper'>
								<view class='iconfont animated'
									:class='(item.icon) + " " + (animated==true&&active==index ?"bounceIn":"")'>
								</view>
								{{item.name}}
							</view>
							<view class="acea-row">
								<view class='tip'>
									{{item.title}}
									<block v-if="item.value === 'yue'">
										{{item.userBalance}}
									</block>
								</view>
								<view class="radio">
									<block v-if="active==index">
										<view class="iconfont icon-xuanzhong1 font-color"></view>
									</block>
									<block v-else>
										<view class="iconfont icon-weixuanzhong"></view>
									</block>
								</view>
							</view>
						</view>
					</block>
				</view>
			</view>
		</view>
		<view v-if="isShow" class="titleNo">暂无支付方式！</view>
		<view class="btn-box">
			<button class='Bnt bg-color' @tap='toOrderPay' :disabled="isBuy">立即支付</button>
		</view>
		<view class="alipaysubmit" v-html="formContent"></view>
	</view>
</template>

<script>
	// +----------------------------------------------------------------------
	// | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
	// +----------------------------------------------------------------------
	// | Copyright (c) 2016~2025 https://www.crmeb.com All rights reserved.
	// +----------------------------------------------------------------------
	// | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
	// +----------------------------------------------------------------------
	// | Author: CRMEB Team <admin@crmeb.com>
	// +----------------------------------------------------------------------
	import {openOrderSubscribe} from '@/utils/SubscribeMessage.js';
	import {
		orderPay,
		wechatQueryPayResult
	} from '@/api/order.js';
	import {
		Debounce
	} from '@/utils/validate.js'
	import {
		mapGetters
	} from "vuex";
	import store from '@/store'
	let app = getApp();
	export default {
		data() {
			return {
				active: null, //支付方式切换
				theme: app.globalData.theme,
				//支付方式
				//支付方式
				cartArr: [],
				payPrice: '',
				orderNo: '',
				animated: false,
				payType: '', //支付方式
				payChannel: '',
				formContent: '',
				isShow: false,
				userBalance: '', //余额
				isBuy: false //是否可以点击购买
			}
		},
		computed: {
			...mapGetters(['productType', 'systemPlatform', 'uid', 'userInfo'])
		},
		onLoad(options) {
			this.payPrice = options.payPrice;
			this.orderNo = options.orderNo;
			console.log('[PAY_DEBUG][payment_onLoad]', {
				options,
				orderNo: this.orderNo,
				payPrice: this.payPrice,
				accountInfo: this.getPayDebugAccountInfo()
			});
		},
		mounted() {
			this.payConfig();
		},
		methods: {
			// 支付配置
			payConfig() {
				uni.hideLoading();
				// 支付方式
				store.dispatch('getPayConfig').then((res) => {
					console.log(res.payConfig);
					console.log('[PAY_DEBUG][payment_payConfig]', {
						payConfig: res.payConfig,
						userBalance: res.userBalance,
						accountInfo: this.getPayDebugAccountInfo()
					});
					this.cartArr = res.payConfig;
					// #ifdef APP
					res.payConfig.forEach(val=>{
						if(val.value==='weixin'){
							val.payStatus=0
						}
					})
					// #endif
					this.userBalance = res.userBalance;
					if (this.cartArr.length) {
						this.active = 0;
						this.payType = this.cartArr[0].value;
						this.isShow = false;
					} else {
						this.isShow = true;
						return this.$util.Tips({
							title: '暂无支付方式！'
						})
					}
				});
			},
			getPayDebugAccountInfo() {
				let miniProgramAppId = '';
				let envVersion = '';
				let version = '';
				// #ifdef MP-WEIXIN
				try {
					const accountInfo = uni.getAccountInfoSync ? uni.getAccountInfoSync() : null;
					miniProgramAppId = accountInfo && accountInfo.miniProgram ? accountInfo.miniProgram.appId : '';
					envVersion = accountInfo && accountInfo.miniProgram ? accountInfo.miniProgram.envVersion : '';
					version = accountInfo && accountInfo.miniProgram ? accountInfo.miniProgram.version : '';
				} catch (e) {
					miniProgramAppId = 'getAccountInfoSync error';
					envVersion = e && (e.errMsg || e.message) ? (e.errMsg || e.message) : '';
				}
				// #endif
				return {
					miniProgramAppId,
					envVersion,
					version,
					storeUid: this.uid,
					userInfoUid: this.userInfo && this.userInfo.uid,
					productType: this.productType,
					systemPlatform: this.systemPlatform
				};
			},
			maskPayDebug(value) {
				if (!value || value.length <= 12) return value;
				return value.substring(0, 6) + '***' + value.substring(value.length - 6);
			},
			getPayDebugJsConfig(jsConfig) {
				if (!jsConfig) return null;
				return {
					appId: jsConfig.appId,
					timeStamp: jsConfig.timeStamp,
					nonceStr: this.maskPayDebug(jsConfig.nonceStr),
					package: jsConfig.packages,
					signType: jsConfig.signType,
					paySign: this.maskPayDebug(jsConfig.paySign),
					partnerid: jsConfig.partnerid,
					ticket: jsConfig.ticket
				};
			},
			getRoutinePayCode() {
				return new Promise(resolve => {
					// #ifdef MP-WEIXIN
					uni.login({
						provider: 'weixin',
						success: res => {
							resolve(res && res.code ? res.code : '');
						},
						fail: err => {
							console.error('[PAY_DEBUG][payment_getRoutinePayCode_fail]', {
								err,
								accountInfo: this.getPayDebugAccountInfo()
							});
							resolve('');
						}
					});
					// #endif
					// #ifndef MP-WEIXIN
					resolve('');
					// #endif
				});
			},
			payItem: Debounce(function(e, item) {
				let that = this;
				if (item.userBalance) that.userBalance = item.userBalance
				let active = e;
				that.active = active;
				that.animated = true;
				that.payType = that.cartArr[active].value;
				console.log('[PAY_DEBUG][payment_payItem]', {
					active,
					selectedPayType: that.payType,
					item,
					userBalance: that.userBalance,
					orderNo: that.orderNo,
					payPrice: that.payPrice,
					accountInfo: that.getPayDebugAccountInfo()
				});
				setTimeout(function() {
					that.car();
				}, 500);
			}),
			car: function() {
				let that = this;
				that.animated = false;
			},
			//选择支付方式的判断，传参
			getPayCheck() {
				if (!this.payType) return this.$util.Tips({
					title: '请选择支付方式'
				});
				if (this.payType === 'yue') {
					this.payChannel = 'yue'
				} else if (this.payType == 'alipay') {
					// #ifdef H5
					this.payChannel = 'alipay';
					// #endif
					// #ifdef APP-PLUS
					this.payChannel = 'appAliPay';
					// #endif
				} else {
					// #ifdef H5
					this.payChannel = this.$wechat.isWeixin() ? 'public' : 'weixinh5';
					// #endif
					// #ifdef APP-PLUS
					this.payChannel = this.systemPlatform === 'ios' ? 'weixinAppIos' : 'weixinAppAndroid';
					// #endif
					// #ifdef MP
					this.payChannel = "routine";
					if (this.productType == 'video') {
						this.payChannel = "video";
					} else {
						this.payChannel = "routine";
					}
					// #endif
				}
				console.log('[PAY_DEBUG][payment_getPayCheck]', {
					orderNo: this.orderNo,
					payPrice: this.payPrice,
					payType: this.payType,
					payChannel: this.payChannel,
					productType: this.productType,
					accountInfo: this.getPayDebugAccountInfo()
				});
			},
			getOrderPay: async function(orderNo, message) {
				let that = this;
				let goPages = '/pages/order/order_pay_status/index?order_id=' + orderNo;
				const routineCode = that.payType === 'weixin' && that.payChannel === 'routine' ? await that.getRoutinePayCode() : '';
				console.log('[PAY_DEBUG][payment_before_orderPay]', {
					orderNo,
					payType: that.payType,
					payChannel: that.payChannel,
					scene: that.productType === 'normal' ? 0 : 1177,
					payPrice: that.payPrice,
					routineCodePresent: !!routineCode,
					accountInfo: that.getPayDebugAccountInfo()
				});
				orderPay({
					orderNo: orderNo,
					payChannel: that.payChannel,
					payType: that.payType,
					scene: that.productType === 'normal' ? 0 : 1177, //下单时小程序的场景值
					routineCode
				}).then(res => {
					let jsConfig = res.data.jsConfig;
					console.log('[PAY_DEBUG][payment_orderPay_response]', {
						orderNo,
						responseOrderNo: res.data.orderNo,
						payType: res.data.payType,
						status: res.data.status,
						jsConfig: that.getPayDebugJsConfig(jsConfig),
						accountInfo: that.getPayDebugAccountInfo()
					});
					switch (res.data.payType) {
						case 'weixin':
							that.weixinPay(jsConfig, orderNo, goPages);
							break;
						case 'yue':
							return that.$util.Tips({
								title: message
							}, {
								tab: 5,
								url: goPages + '&status=1'
							});
							uni.hideLoading();
							break;
						case 'weixinh5':
							setTimeout(() => {
								location.href = jsConfig.mwebUrl + '&redirect_url=' +
									window.location
									.protocol + '//' + window.location.host + goPages +
									'&status=1';
							}, 100)
							uni.hideLoading();
							break;
					}
				}).catch(err => {
					console.error('[PAY_DEBUG][payment_orderPay_error]', {
						orderNo,
						err,
						payType: that.payType,
						payChannel: that.payChannel,
						accountInfo: that.getPayDebugAccountInfo()
					});
					uni.hideLoading();
					return that.$util.Tips({
						title: err
					});
				});
			},
			weixinPay(jsConfig, orderNo, goPages) {
				let that = this;
				console.log('[PAY_DEBUG][payment_before_weixinPay]', {
					orderNo,
					goPages,
					api: that.productType === 'video' ? 'uni.requestOrderPayment' : 'uni.requestPayment',
					jsConfig: that.getPayDebugJsConfig(jsConfig),
					accountInfo: that.getPayDebugAccountInfo()
				});
				// #ifdef MP
				if (that.productType === 'video') {
					uni.requestOrderPayment({
						timeStamp: jsConfig.timeStamp,
						nonceStr: jsConfig.nonceStr,
						package: jsConfig.packages,
						signType: jsConfig.signType,
						paySign: jsConfig.paySign,
						ticket: jsConfig.ticket,
						success: function(ress) {
							console.log('[PAY_DEBUG][payment_requestOrderPayment_success]', {
								orderNo,
								result: ress,
								accountInfo: that.getPayDebugAccountInfo()
							});
							uni.hideLoading();
							openOrderSubscribe().then(() => {
								return that.$util.Tips({
									title: '支付成功',
									icon: 'success'
								}, {
									tab: 5,
									url: goPages
								}, );
							})
						},
						fail: function(e) {
							console.error('[PAY_DEBUG][payment_requestOrderPayment_fail]', {
								orderNo,
								err: e,
								jsConfig: that.getPayDebugJsConfig(jsConfig),
								accountInfo: that.getPayDebugAccountInfo()
							});
							uni.hideLoading();
							return that.$util.Tips({
								title: '取消支付'
							}, {
								tab: 5,
								url: goPages + '&status=2'
							});
						},
						complete: function(e) {
							console.log('[PAY_DEBUG][payment_requestOrderPayment_complete]', {
								orderNo,
								result: e,
								jsConfig: that.getPayDebugJsConfig(jsConfig),
								accountInfo: that.getPayDebugAccountInfo()
							});
							uni.hideLoading();
							//关闭当前页面跳转至订单状态
							if (e.errMsg == 'requestPayment:cancel') return that.$util.Tips({
								title: '取消支付'
							}, {
								tab: 5,
								url: goPages + '&status=2'
							});
						},
					})
				} else {
					uni.requestPayment({
						timeStamp: jsConfig.timeStamp,
						nonceStr: jsConfig.nonceStr,
						package: jsConfig.packages,
						signType: jsConfig.signType,
						paySign: jsConfig.paySign,
						//ticket: jsConfig.ticket,
						success: function(ress) {
							console.log('[PAY_DEBUG][payment_requestPayment_success]', {
								orderNo,
								result: ress,
								accountInfo: that.getPayDebugAccountInfo()
							});
							uni.hideLoading();
							openOrderSubscribe().then(() => {
								return that.$util.Tips({
									title: '支付成功',
									icon: 'success'
								}, {
									tab: 5,
									url: goPages
								}, );
							})
						},
						fail: function(e) {
							console.error('[PAY_DEBUG][payment_requestPayment_fail]', {
								orderNo,
								err: e,
								jsConfig: that.getPayDebugJsConfig(jsConfig),
								accountInfo: that.getPayDebugAccountInfo()
							});
							uni.hideLoading();
							return that.$util.Tips({
								title: '取消支付'
							}, {
								tab: 5,
								url: goPages + '&status=2'
							});
						},
						complete: function(e) {
							console.log('[PAY_DEBUG][payment_requestPayment_complete]', {
								orderNo,
								result: e,
								jsConfig: that.getPayDebugJsConfig(jsConfig),
								accountInfo: that.getPayDebugAccountInfo()
							});
							uni.hideLoading();
							//关闭当前页面跳转至订单状态
							if (e.errMsg == 'requestPayment:cancel') return that.$util.Tips({
								title: '取消支付'
							}, {
								tab: 5,
								url: goPages + '&status=2'
							});
						},
					})
				}
			
				// #endif
				// #ifdef H5
				let data = {
					timestamp: jsConfig.timeStamp,
					nonceStr: jsConfig.nonceStr,
					package: jsConfig.packages,
					signType: jsConfig.signType,
					paySign: jsConfig.paySign
				};
				that.$wechat.pay(data).then(res => {
					if (res.errMsg == 'chooseWXPay:cancel') {
						uni.hideLoading();
						return that.$util.Tips({
							title: '取消支付'
						}, {
							tab: 5,
							url: goPages + '&status=2'
						});
					} else {
						console.log('[PAY_DEBUG][payment_h5_pay_success_before_query]', {
							orderNo,
							result: res,
							accountInfo: that.getPayDebugAccountInfo()
						});
						wechatQueryPayResult(orderNo).then(res => {
							console.log('[PAY_DEBUG][payment_h5_query_success]', {
								orderNo,
								result: res,
								accountInfo: that.getPayDebugAccountInfo()
							});
							uni.hideLoading();
							return that.$util.Tips({
								title: '支付成功',
								icon: 'success'
							}, {
								tab: 5,
								url: goPages
							});
						}).catch(err => {
							console.error('[PAY_DEBUG][payment_h5_query_error]', {
								orderNo,
								err,
								accountInfo: that.getPayDebugAccountInfo()
							});
							uni.hideLoading();
							return that.$util.Tips({
								title: err
							});
						})
					}
				}).catch(res => {
					console.error('[PAY_DEBUG][payment_h5_pay_error]', {
						orderNo,
						err: res,
						accountInfo: that.getPayDebugAccountInfo()
					});
					uni.hideLoading();
					return that.$util.Tips({
						title: '取消支付'
					}, {
						tab: 5,
						url: goPages + '&status=2'
					});
				});
				// #endif
				// #ifdef APP-PLUS
				uni.requestPayment({
					provider: 'wxpay',
					orderInfo: {
						"appid": jsConfig.appId, // 微信开放平台 - 应用 - AppId，注意和微信小程序、公众号 AppId 可能不一致
						"noncestr": jsConfig.nonceStr, // 随机字符串
						"package": "Sign=WXPay", // 固定值
						"partnerid": jsConfig.partnerid, // 微信支付商户号
						"prepayid": jsConfig.packages, // 统一下单订单号
						"timestamp": Number(jsConfig.timeStamp), // 时间戳（单位：秒）
						"sign": this.systemPlatform === 'ios' ? 'MD5' : jsConfig
							.paySign // 签名，这里用的 MD5 签名
					}, //订单数据 【注意微信的订单信息，键值应该全部是小写，不能采用驼峰命名】
					success: function(res) {
						wechatQueryPayResult(orderNo).then(res => {
							uni.hideLoading();
							let url = '/pages/order/order_pay_status/index?order_id=' + orderNo +
								'&msg=支付成功';
							uni.showToast({
								title: "支付成功"
							})
							setTimeout(res => {
								uni.redirectTo({
									url: url
								})
							}, 2000)
						}).catch(err => {
							uni.hideLoading();
							return that.$util.Tips({
								title: err
							});
						})
					},
					fail: function(err) {
						uni.hideLoading();
						let url = '/pages/order/order_pay_status/index?order_id=' + orderNo +
							'&msg=支付失败';
						uni.showModal({
							content: "支付失败",
							showCancel: false,
							success: function(res) {
								if (res.confirm) {
									uni.redirectTo({
										url: url
									})
								}
							}
						})
					},
					complete: (err) => {
						uni.hideLoading();
					}
				});
				// #endif
			},
			//立即支付
			toOrderPay: Debounce(function() {
				this.getPayCheck();
				if (Number(this.payPrice) > Number(this.userBalance) && this.payType === 'yue') return this.$util
					.Tips({
						title: '余额的金额不够，请切换支付方式'
					});
				uni.showLoading({
					title: '加载中...'
				});
				this.isBuy = true;
				this.getOrderPay(this.orderNo, '支付成功')
			})
		},
	}
</script>

<style lang="scss" scoped>
	.titleNo {
		width: 100%;
		font-size: 28rpx;
		text-align: center;
	}

	.btn-box {
		padding: 0 30rpx;
		position: fixed;
		bottom: 43rpx;
	}

	.Bnt {
		font-size: 30rpx;
		font-weight: bold;
		color: #fff;
		width: 690rpx;
		height: 86rpx;
		border-radius: 43rpx;
		text-align: center;
		line-height: 86rpx;
	}

	.wrapper {

		padding: 30rpx;

		.list {
			margin-top: 50rpx;
		}

		.item {
			padding: 50rpx 30rpx;
			font-size: 30rpx;
			color: #333333;
			background-color: #fff;

			.title {
				text-align: center;
				@include main_color(theme);
				font-size: 34rpx;

				text {
					font-weight: 800;
					font-size: 50rpx;
				}
			}
		}

		.payItem {
			border-bottom: 1px solid #eee;
			justify-content: space-between;
			height: 138rpx;
			line-height: 138rpx;
			width: 100%;
			box-sizing: border-box;
			font-size: 32pxrpx;
			color: #333333;

			.on {
				// border-color: #fc5445;
				@include coupons_border_color(theme);
				color: $theme-color;
			}

			.name {

				.iconfont {
					width: 48rpx;
					height: 48rpx;
					border-radius: 50%;
					text-align: center;
					line-height: 48rpx;
					background-color: #fe960f;
					color: #fff;
					font-size: 30rpx;
					margin-right: 28rpx;
				}
			}

			.iconfont.icon-weixinzhifu1 {
				background-color: #41b035;
			}

			.iconfont.icon-zhifubao {
				background-color: #00AAEA;
			}

			.tip {
				text-align: center;
				font-size: 26rpx;
				color: #aaa;
				margin-right: 20rpx;
			}

			.radio {
				.iconfont {
					font-size: 46rpx;
				}
			}
		}
	}
</style>
