package com.example.quartz.convert;

import com.example.quartz.model.bo.ScheduleAddBo;
import com.example.quartz.model.bo.ScheduleEditBo;
import com.example.quartz.model.entity.Schedule;
import com.example.quartz.model.vo.ScheduleVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ScheduleConvert {

    ScheduleConvert INSTANCE = Mappers.getMapper(ScheduleConvert.class);

    ScheduleVo scheduleToScheduleVo(Schedule schedule);

    List<ScheduleVo> scheduleListToScheduleVoList(List<Schedule> records);

    Schedule scheduleAddBoToSchedule(ScheduleAddBo scheduleAddBo);

    Schedule scheduleEditBoToSchedule(ScheduleEditBo scheduleeditBo);

}

