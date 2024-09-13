package plugin.oremininggame.mapper.data;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface PlayerScoreMapper {

  @Select("select * from player_score")
  List<PlayerScore> selectList();

  @Insert("insert player_score(player_name, score, registered_at) values (#{playerName},#{score},now())")
  int insert(PlayerScore playerScore);
}
