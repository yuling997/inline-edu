<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- 讲师列表 开始 -->
    <section class="container">
      <section class="c-sort-box unBr">
        <div>
          <!-- /无数据提示 开始-->
          <section v-if="data.total==0" class="no-data-wrap">
            <em class="icon30 no-data-ico">&nbsp;</em>
            <span class="c-666 fsize14 ml10 vam">没有相关数据，小编正在努力整理中...</span>
          </section>
          <!-- /无数据提示 结束-->

          <!-- /数据列表 开始-->
          <article v-if="data.total>0" class="i-teacher-list">
            <ul class="of">
              <li v-for="item in data.items" :key="item.id">
                <section class="i-teach-wrap">
                  <div class="i-teach-pic">
                    <a :href="'/teacher/'+item.id" :title="item.name">
                      <img :src="item.avatar" :alt="item.name" height="142" hright="142">
                    </a>
                  </div>
                  <div class="mt10 hLh30 txtOf tac">
                    <a :href="'/teacher/'+item.id" :title="item.name" class="fsize18 c-666">{{ item.name }}</a>
                  </div>
                  <div class="hLh30 txtOf tac">
                    <span class="fsize14 c-999" >{{ item.career }}</span>
                  </div>
                  <div class="mt15 i-q-txt">
                    <p class="c-999 f-fA">{{ item.intro }}</p>
                  </div>
                </section>
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
    <!-- /讲师列表 结束 -->
  </div>
</template>

<script>
import teacherApi from '~/api/teacher'
export default {
// 定义同步获取的数据模型
  async asyncData({ route }) {
    // console.log('asyncData')
    let page = route.query.page// 通过浏览器的url地址栏传递分页参数
    if (!page) {
      page = 1
    }
    const response = await teacherApi.getPageList(page, 8)
    // console.log(response)
    return {
      data: response.data.data
    }
  },

  methods: {
    gotoPage(page) {
      // this.$route.query.page = page
      // const querys = querystring.stringify(this.$route.query)
      // window.location = '/teacher?' + querys
      window.location = '/teacher?page=' + page
    }
  }
}
</script>
