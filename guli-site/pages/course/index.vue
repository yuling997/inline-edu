<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- 课程列表 开始 -->
    <section class="container">
      <header class="comm-title">
        <h2 class="fl tac">
          <span class="c-333">全部课程</span>
        </h2>
      </header>
      <section class="c-sort-box">
        <section class="c-s-dl">
          <dl>
            <dt>
              <span class="c-999 fsize14">课程类别</span>
            </dt>
            <dd class="c-s-dl-li">
              <!-- 一级类别 开始-->
              <ul class="clearfix">
                <li
                  :class="{current:!$route.query.subjectParentId}">
                  <a
                    title="全部"
                    href="javascript:void(0);"
                    @click="searchSubjectLevelOne('')">全部</a>
                </li>
                <li
                  v-for="item in subjectNestedList"
                  :key="item.id"
                  :class="{current:$route.query.subjectParentId==item.id}">
                  <a
                    :title="item.title"
                    href="javascript:void(0);"
                    @click="searchSubjectLevelOne(item.id)">{{ item.title }}</a>
                </li>
              </ul>
              <!-- /一级类别 结束-->
            </dd>
          </dl>
          <dl>
            <dt>
              <span class="c-999 fsize14"/>
            </dt>
            <dd class="c-s-dl-li">
              <!-- 二级类别 开始-->
              <ul class="clearfix">
                <li
                  v-for="item in subSubjectList"
                  :key="item.id"
                  :class="{current:$route.query.subjectId==item.id}">
                  <a
                    :title="item.title"
                    href="javascript:void(0);"
                    @click="searchSubjectLevelTwo(item.id)">{{ item.title }}</a>
                </li>
              </ul>
              <!-- /二级类别 结束-->
            </dd>
          </dl>
          <div class="clear"/>
        </section>
        <div class="js-wrap">
          <section class="fr">
            <span class="c-ccc">
              <i class="c-master f-fM">1</i>/
              <i class="c-666 f-fM">1</i>
            </span>
          </section>
          <section class="fl">
            <!-- 排序 开始-->
            <ol class="js-tap clearfix">
              <li :class="{'current bg-orange':searchObj.buyCountSort!=''}">
                <a title="销量" href="javascript:void(0);" @click="searchBuyCount()">销量
                  <span :class="{undis:searchObj.buyCountSort==''}">↓</span>
                </a>
              </li>
              <li :class="{'current bg-orange':searchObj.gmtCreateSort!=''}">
                <a title="最新" href="javascript:void(0);" @click="searchGmtCreate()">最新
                  <span :class="{undis:searchObj.gmtCreateSort==''}">↓</span>
                </a>
              </li>
              <li :class="{'current bg-green':searchObj.priceSort!=''}">
                <a title="价格" href="javascript:void(0);" @click="searchPrice()">价格&nbsp;
                  <span :class="{undis:searchObj.priceSort==''}">↓</span>
                </a>
              </li>
            </ol>
            <!-- /排序 结束-->
          </section>
        </div>
        <div class="mt40">
          <!-- 无数据提示 开始-->
          <section v-if="data.total==0" class="no-data-wrap">
            <em class="icon30 no-data-ico">&nbsp;</em>
            <span class="c-666 fsize14 ml10 vam">没有相关数据，小编正在努力整理中...</span>
          </section>
          <!-- /无数据提示 结束-->

          <!-- 数据列表 开始-->
          <article v-if="data.total>0" class="comm-course-list">
            <ul id="bna" class="of">
              <li v-for="item in data.items" :key="item.id">
                <div class="cc-l-wrap">
                  <section class="course-img">
                    <img :src="item.cover" :alt="item.title" class="img-responsive">
                    <div class="cc-mask">
                      <a :href="'/course/'+item.id" title="开始学习" class="comm-btn c-btn-1">开始学习</a>
                    </div>
                  </section>
                  <h3 class="hLh30 txtOf mt10">
                    <a :href="'/course/'+item.id" :title="item.title" class="course-title fsize18 c-333">{{ item.title }}</a>
                  </h3>
                  <section class="mt10 hLh20 of">
                    <span v-if="Number(item.price) === 0" class="fr jgTag bg-green">
                      <i class="c-fff fsize12 f-fA">免费</i>
                    </span>
                    <span v-else class="fr jgTag ">
                      <i class="c-orange fsize12 f-fA"> ￥{{ item.price }}</i>
                    </span>
                    <span class="fl jgAttr c-ccc f-fA">
                      <i class="c-999 f-fA">{{ item.viewCount }}人学习</i>
                      |
                      <i class="c-999 f-fA">{{ item.buyCount }}人购买</i>
                    </span>
                  </section>
                </div>
              </li>
            </ul>
            <div class="clear"/>
          </article>
          <!-- /数据列表 结束-->
        </div>
        <!-- 公共分页 开始 -->
        <div>
          <div class="paging">
            <!-- 首页 -->
            <!-- undisable这个class是否存在，取决于数据属性hasPrevious -->
            <a
              v-if="data.hasPrevious"
              href="javascript:void(0);"
              title="首页"
              @click="gotoPage(1)">首</a>
            <a
              v-if="!data.hasPrevious"
              class="undisable"
              title="首页">首</a>
            <!-- 前一页 -->
            <a
              v-if="data.hasPrevious"
              href="javascript:void(0);"
              title="前一页"
              @click="gotoPage(data.current-1)">&lt;</a>
            <a
              v-if="!data.hasPrevious"
              class="undisable"
              title="前一页">&lt;</a>
            <!-- 分页 -->
            <a
              v-for="page in data.pages"
              :key="page"
              :class="{current: data.current == page, undisable: data.current == page}"
              :title="'第'+page+'页'"
              href="javascript:void(0);"
              @click="gotoPage(page)">{{ page }}</a>

            <!-- 后一页 -->
            <a
              v-if="data.hasNext"
              href="javascript:void(0);"
              title="后一页"
              @click="gotoPage(data.current+1)">&gt;</a>
            <a
              v-if="!data.hasNext"
              class="undisable"
              title="后一页">&gt;</a>
            <!-- 末页 -->
            <a
              v-if="data.hasNext"
              href="javascript:void(0);"
              title="末页"
              @click="gotoPage(data.pages)">末</a>
            <a
              v-if="!data.hasNext"
              class="undisable"
              title="末页">末</a>
            <div class="clear"/>
          </div>
        </div>
        <!-- /公共分页 结束 -->
      </section>
    </section>
    <!-- /课程列表 结束 -->
  </div>
</template>

<script>

import subjectApi from '~/api/subject'
import querystring from 'querystring'
import courseApi from '~/api/course'

export default {

  async asyncData({ route }) {
    const searchObj = {}
    searchObj.page = route.query.page || 1
    searchObj.limit = 8
    searchObj.subjectParentId = route.query.subjectParentId || ''
    searchObj.subjectId = route.query.subjectId || ''
    searchObj.buyCountSort = route.query.buyCountSort || ''
    searchObj.gmtCreateSort = route.query.gmtCreateSort || ''
    searchObj.priceSort = route.query.priceSort || ''

    // 课程分类
    const subjectNestedListResponse = await subjectApi.getSubjectNestedTreeList()
    const subjectNestedList = subjectNestedListResponse.data.data.items
    let subSubjectList = []
    for (let i = 0; i < subjectNestedList.length; i++) {
      if (subjectNestedList[i].id === searchObj.subjectParentId) {
        subSubjectList = subjectNestedList[i].children
      }
    }

    // 分页课程列表
    const courseListResponse = await courseApi.getPageList(searchObj)
    return {
      subjectNestedList: subjectNestedList,
      subSubjectList: subSubjectList,
      // data: courseListResponse.data.data,
      searchObj: searchObj,
      data: courseListResponse.data.data
    }
  },

  methods: {

    // 一级分类事件方法
    searchSubjectLevelOne(subjectParentId) {
      console.log('searchSubjectLevelOne')
      this.searchObj.subjectParentId = subjectParentId
      this.searchObj.subjectId = ''
      const querys = querystring.stringify(this.searchObj)
      // console.log(querys)
      window.location = '/course?' + querys
    },

    // 二级分类事件方法
    searchSubjectLevelTwo(subjectId) {
      console.log('searchSubjectLevelTwo')
      this.searchObj.subjectId = subjectId
      const querys = querystring.stringify(this.searchObj)
      // console.log(querys)
      window.location = '/course?' + querys
    },

    // 销售事件方法
    searchBuyCount() {
      this.searchObj.buyCountSort = '1'
      this.searchObj.gmtCreateSort = ''
      this.searchObj.priceSort = ''
      const querys = querystring.stringify(this.searchObj)
      // console.log(querys)
      window.location = '/course?' + querys
    },

    searchGmtCreate() {
      this.searchObj.buyCountSort = ''
      this.searchObj.gmtCreateSort = '1'
      this.searchObj.priceSort = ''
      const querys = querystring.stringify(this.searchObj)
      // console.log(querys)
      window.location = '/course?' + querys
    },

    searchPrice() {
      this.searchObj.buyCountSort = ''
      this.searchObj.gmtCreateSort = ''
      this.searchObj.priceSort = '1'
      const querys = querystring.stringify(this.searchObj)
      // console.log(querys)
      window.location = '/course?' + querys
    },

    gotoPage(page) {
      console.log('gotoPage')
      this.searchObj.page = page
      const querys = querystring.stringify(this.searchObj)
      // console.log(querys)
      window.location = '/course?' + querys
    }
  }
}
</script>
