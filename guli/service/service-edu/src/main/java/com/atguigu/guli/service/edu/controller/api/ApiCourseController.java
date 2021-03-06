package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseQueryVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@Api(description="课程")
@RestController
@RequestMapping("/web/edu/course")
public class ApiCourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ChapterService chapterService;

    @ApiOperation(value = "分页课程列表")
    @GetMapping(value = "page-list")
    public R pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @RequestParam Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @RequestParam Long limit,

            @ApiParam(name = "webCourseQueryVo", value = "查询对象", required = false)
            WebCourseQueryVo webCourseQueryVo
    ) {

        Page<Course> pageParam = new Page<>(page, limit);
        Map<String, Object> map = courseService.webSelectPage(pageParam, webCourseQueryVo);

        return  R.ok().data(map);
    }

    @ApiOperation(value = "根据ID查询课程")
    @GetMapping(value = "get/{courseId}")
    public R getById(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId
            ) {

        //查询课程信息和讲师信息
        WebCourseVo webCourseVo = courseService.selectWebCourseVoById(courseId);

        //查询当前课程的章节信息
        List<ChapterVo> chapterVoList = chapterService.nestedList(courseId);

        return R.ok().data("course", webCourseVo).data("chapterVoList", chapterVoList);
    }

    @ApiOperation(value = "根据课程id查询课程信息")
    @GetMapping(value = "inner/get-course-dto/{courseId}")
    public CourseDto getCourseDtoById(@PathVariable String courseId){

        CourseDto courseDto = courseService.getCourseDtoById(courseId);
        return courseDto;
    }
}
