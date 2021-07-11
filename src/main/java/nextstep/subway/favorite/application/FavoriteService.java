package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public Favorite save(LoginMember loginMember, FavoriteRequest request) {
        Station sourceStation = stationService.findStationById(request.getSourceId());
        Station targetStation = stationService.findStationById(request.getSourceId());

        Member member = memberService.findByIdOrThrow(loginMember.getId());

        Favorite favorite = new Favorite(sourceStation, targetStation, member);

        return favoriteRepository.save(favorite);
    }
}
