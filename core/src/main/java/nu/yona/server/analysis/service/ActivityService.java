package nu.yona.server.analysis.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import nu.yona.server.analysis.entities.DayActivity;
import nu.yona.server.analysis.entities.WeekActivity;
import nu.yona.server.properties.YonaProperties;
import nu.yona.server.subscriptions.service.UserAnonymizedDTO;
import nu.yona.server.subscriptions.service.UserAnonymizedService;
import nu.yona.server.subscriptions.service.UserService;

@Service
public class ActivityService
{
	@Autowired
	private UserService userService;

	@Autowired
	private UserAnonymizedService userAnonymizedService;

	@Autowired
	private YonaProperties yonaProperties;

	public Page<WeekActivityOverviewDTO> getWeekActivityOverviews(UUID userID, Pageable pageable)
	{
		UUID userAnonymizedID = userService.getPrivateUser(userID).getPrivateData().getUserAnonymizedID();
		UserAnonymizedDTO userAnonymized = userAnonymizedService.getUserAnonymized(userAnonymizedID);
		LocalDate dateFrom = LocalDate.now(ZoneId.of(userAnonymized.getTimeZoneId())).minusWeeks(pageable.getOffset());
		LocalDate dateUntil = dateFrom.minusWeeks(pageable.getPageSize());
		Set<WeekActivity> weekActivityEntities = WeekActivity.getRepository().findAll(userAnonymizedID, dateUntil, dateFrom);
		Map<LocalDate, Set<WeekActivity>> weekActivityEntitiesByDate = weekActivityEntities.stream()
				.collect(Collectors.groupingBy(a -> a.getDate(), Collectors.toSet()));
		// TODO: add zero time items for living goals that recorded no activity
		return new PageImpl<WeekActivityOverviewDTO>(weekActivityEntitiesByDate.entrySet().stream()
				.map(e -> WeekActivityOverviewDTO.createInstance(e.getValue())).collect(Collectors.toList()), pageable,
				yonaProperties.getAnalysisService().getWeeksActivityMemory());
	}

	public Page<DayActivityDTO> getDayActivity(UUID userID, Pageable pageable)
	{
		UUID userAnonymizedID = userService.getPrivateUser(userID).getPrivateData().getUserAnonymizedID();
		Page<DayActivity> dayActivityEntitiesPage = DayActivity.getRepository().findAll(userAnonymizedID, pageable);
		return new PageImpl<DayActivityDTO>(dayActivityEntitiesPage.getContent().stream()
				.map(a -> DayActivityDTO.createInstance(a)).collect(Collectors.toList()), pageable,
				dayActivityEntitiesPage.getTotalElements());
	}

	public WeekActivityDTO getWeekActivity(UUID userID, LocalDate date, UUID goalID)
	{
		UUID userAnonymizedID = userService.getPrivateUser(userID).getPrivateData().getUserAnonymizedID();
		return WeekActivityDTO.createInstance(WeekActivity.getRepository().findOne(userAnonymizedID, date, goalID));
	}

	public DayActivityDTO getDayActivity(UUID userID, LocalDate date, UUID goalID)
	{
		UUID userAnonymizedID = userService.getPrivateUser(userID).getPrivateData().getUserAnonymizedID();
		return DayActivityDTO.createInstance(DayActivity.getRepository().findOne(userAnonymizedID, date, goalID));
	}
}
