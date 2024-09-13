package plugin.oremininggame.mapper.data;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PlayerScore {

  private int id;
  private String playerName;
  private int score;
  private LocalDateTime registeredAt;

  public PlayerScore(String playerName, int score) {
    this.playerName = playerName;
    this.score = score;
  }
}
