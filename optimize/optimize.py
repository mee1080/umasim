import subprocess
import optuna

def objective(trial):
    speed = trial.suggest_uniform('speed', 0.2, 2.0)
    stamina = trial.suggest_uniform('stamina', 0.2, 2.0)
    power = trial.suggest_uniform('power', 0.2, 2.0)
    guts = trial.suggest_uniform('guts', 0.2, 2.0)
    wisdom = trial.suggest_uniform('wisdom', 0.2, 2.0)
    skillPt = trial.suggest_uniform('skillPt', 0.1, 1.0)
    hp = trial.suggest_uniform('hp', 0.6, 1.5)
#    aoharuJunior = trial.suggest_uniform('aoharuJunior', 0.0, 30.0)
#    aoharuClassic1 = trial.suggest_uniform('aoharuClassic1', 0.0, 30.0)
#    aoharuClassic2 = trial.suggest_uniform('aoharuClassic2', 0.0, 30.0)
#    aoharuSenior = trial.suggest_uniform('aoharuSenior', 0.0, 30.0)
#    aoharuBurn = trial.suggest_uniform('aoharuBurn', 0.0, 30.0)

#    cmd = f'java -jar ../cli/build/libs/cli.jar --count 50000 --chara "[超特急！フルカラー特殊PP]アグネスデジタル" 5 5 --support "[迫る熱に押されて]キタサンブラック" 4 --support "[袖振り合えば福となる♪]マチカネフクキタル" 4 --support "[感謝は指先まで込めて]ファインモーション" 4 --support "[願いまでは拭わない]ナイスネイチャ" 4 --support "[幸せは曲がり角の向こう]ライスシャワー" 4 --support "[徹底管理主義]樫本理子" 4 --speed {speed} --stamina {stamina} --power {power} --guts {guts} --wisdom {wisdom} --skill-pt {skillPt} --hp {hp} --motivation 0.25 --relation SPEED 0 {relationSpeed1} --relation SPEED 1 {relationSpeed2} --relation POWER 0 {relationPower} --relation WISDOM 0 {relationWisdom1} --relation WISDOM 1 {relationWisdom2} --aoharu 24 {aoharuJunior} --aoharu 36 {aoharuClassic1} --aoharu 48 {aoharuClassic2} --aoharu-default {aoharuSenior} --aoharu-burn {aoharuBurn} --factor STAMINA 3 --factor STAMINA 3 --factor STAMINA 3 --factor STAMINA 3 --factor POWER 3 --factor POWER 3'

#    cmd = f'java -jar ../cli/build/libs/cli.jar --count 50000 --chara "[超特急！フルカラー特殊PP]アグネスデジタル" 5 5 --support "[迫る熱に押されて]キタサンブラック" 4 --support "[袖振り合えば福となる♪]マチカネフクキタル" 4 --support "[感謝は指先まで込めて]ファインモーション" 4 --support "[一粒の安らぎ]スーパークリーク" 4 --support "[そこに“いる”幸せ]アグネスデジタル" 4 --support "[徹底管理主義]樫本理子" 4 --speed {speed} --stamina {stamina} --power {power} --guts {guts} --wisdom {wisdom} --skill-pt {skillPt} --hp {hp} --motivation 0.25 --relation SPEED 0 {relationSpeed1} --relation SPEED 1 {relationSpeed2} --relation POWER 0 {relationPower} --relation WISDOM 0 {relationWisdom1} --relation WISDOM 1 {relationWisdom2} --aoharu 24 {aoharuJunior} --aoharu 36 {aoharuClassic1} --aoharu 48 {aoharuClassic2} --aoharu-default {aoharuSenior}'

    relationSpeed1 = trial.suggest_uniform('relationSpeed1', 0.2, 20.0)
    relationSpeed2 = trial.suggest_uniform('relationSpeed2', 0.2, 20.0)
#    relationSpeed3 = trial.suggest_uniform('relationSpeed3', 0.2, 20.0)
#    relationStamina1 = trial.suggest_uniform('relationStamina1', 0.2, 20.0)
#    relationStamina2 = trial.suggest_uniform('relationStamina2', 0.2, 20.0)
#    relationStamina3 = trial.suggest_uniform('relationStamina3', 0.2, 20.0)
    relationPower1 = trial.suggest_uniform('relationPower1', 0.2, 20.0)
#    relationPower2 = trial.suggest_uniform('relationPower2', 0.2, 20.0)
#    relationGuts1 = trial.suggest_uniform('relationGuts1', 0.2, 20.0)
#    relationGuts2 = trial.suggest_uniform('relationGuts2', 0.2, 20.0)
#    relationGuts3 = trial.suggest_uniform('relationGuts3', 0.2, 20.0)
#    relationGuts4 = trial.suggest_uniform('relationGuts4', 0.2, 20.0)
    relationWisdom1 = trial.suggest_uniform('relationWisdom1', 0.2, 20.0)
    relationWisdom2 = trial.suggest_uniform('relationWisdom2', 0.2, 20.0)
#    relationWisdom3 = trial.suggest_uniform('relationWisdom3', 0.2, 20.0)
    relationFriend1 = trial.suggest_uniform('relationFriend1', 0.2, 20.0)

#    cmd = f'java -jar ../cli/build/libs/cli.jar --count 20000 --chara "[初うらら♪さくさくら]ハルウララ" 4 5 --support "[In my way]トーセンジョーダン" 4 --support "[見習い魔女と長い夜]スイープトウショウ" 4 --support "[夕焼けはあこがれの色]スペシャルウィーク" 4 --support "[スノウクリスタル・デイ]マーベラスサンデー" 4 --support "[幽霊さんとハロウィンの魔法]ミホノブルボン" 4 --support "[夜に暁、空に瑞星]アドマイヤベガ" 4 --speed {speed} --stamina {stamina} --power {power} --guts {guts} --wisdom {wisdom} --skill-pt {skillPt} --hp {hp} --motivation 0.25 --relation SPEED 0 {relationSpeed1} --relation SPEED 1 {relationSpeed2} --relation SPEED 2 {relationSpeed3} --relation POWER 0 {relationPower1} --relation POWER 1 {relationPower2} --relation WISDOM 0 {relationWisdom} --aoharu 24 {aoharuJunior} --aoharu 36 {aoharuClassic1} --aoharu 48 {aoharuClassic2} --aoharu-default {aoharuSenior} --factor STAMINA 2 --factor SPEED 3 --factor SPEED 3 --factor STAMINA 2 --factor POWER 3 --factor POWER 3'

#    cmd = f'java -jar ../cli/build/libs/cli.jar --count 20000 --scenario CLIMAX --distance long --chara "[秋桜ダンツァトリーチェ]ゴールドシチー" 4 5 --support "[迫る熱に押されて]キタサンブラック" 4 --support "[袖振り合えば福となる♪]マチカネフクキタル" 4 --support "[はやい！うまい！はやい！]サクラバクシンオー" 4 --support "[一粒の安らぎ]スーパークリーク" 4 --support "[その背中を越えて]サトノダイヤモンド" 4 --support "[今ぞ盛りのさくら花]サクラチヨノオー" 4 --speed {speed} --stamina {stamina} --power {power} --guts {guts} --wisdom {wisdom} --skill-pt {skillPt} --hp {hp} --motivation 0.25 --relation SPEED 0 {relationSpeed1} --relation SPEED 1 {relationSpeed2} --relation SPEED 2 {relationSpeed3} --relation STAMINA 0 {relationStamina1} --relation STAMINA 1 {relationStamina2} --relation STAMINA 2 {relationStamina3} --factor POWER 3 --factor POWER 3 --factor POWER 3 --factor WISDOM 3 --factor WISDOM 3 --factor WISDOM 3'

#    cmd = f'java -jar ../cli/build/libs/cli.jar --count 20000 --scenario CLIMAX --distance mile --chara "[秋桜ダンツァトリーチェ]ゴールドシチー" 4 5 --support "[一等星を目指して]アドマイヤベガ" 4 --support "[飛び出せ、キラメケ]アイネスフウジン" 4 --support "[うらら～な休日]ハルウララ" 4 --support "[バカと笑え]メジロパーマー" 4 --support "[感謝は指先まで込めて]ファインモーション" 4 --support "[願いまでは拭わない]ナイスネイチャ" 4 --speed {speed} --stamina {stamina} --power {power} --guts {guts} --wisdom {wisdom} --skill-pt {skillPt} --hp {hp} --motivation 0.25 --relation GUTS 0 {relationGuts1} --relation GUTS 1 {relationGuts2} --relation GUTS 2 {relationGuts3} --relation GUTS 3 {relationGuts4} --relation WISDOM 0 {relationWisdom1} --relation WISDOM 1 {relationWisdom2} --factor POWER 3 --factor POWER 3 --factor POWER 3 --factor POWER 3 --factor WISDOM 3 --factor WISDOM 3'

#    cmd = f'java -jar ../cli/build/libs/cli.jar --count 20000 --scenario CLIMAX --distance mile --chara "[秋桜ダンツァトリーチェ]ゴールドシチー" 4 5 --support "[桃色のバックショット]ナリタトップロード" 4 --support "[迫る熱に押されて]キタサンブラック" 4 --support "[袖振り合えば福となる♪]マチカネフクキタル" 4 --support "[徹底管理主義]樫本理子" 4 --support "[感謝は指先まで込めて]ファインモーション" 4 --support "[小さなカップに想いをこめて]ニシノフラワー" 4 --speed {speed} --stamina {stamina} --power {power} --guts {guts} --wisdom {wisdom} --skill-pt {skillPt} --hp {hp} --motivation 0.25 --relation SPEED 0 {relationSpeed1} --relation SPEED 1 {relationSpeed2} --relation SPEED 2 {relationSpeed3} --relation WISDOM 0 {relationWisdom1} --relation WISDOM 1 {relationWisdom2} --relation WISDOM 2 {relationWisdom3} --factor POWER 3 --factor POWER 3 --factor POWER 3 --factor POWER 3 --factor POWER 3 --factor STAMINA 3'

    """
    cmd = f'java -jar ../cli/build/libs/cli.jar --count 20000 --scenario CLIMAX'\
          f' --distance mile --chara "[秋桜ダンツァトリーチェ]ゴールドシチー" 4 5'\
          f' --support "[桃色のバックショット]ナリタトップロード" 4'\
          f' --support "[迫る熱に押されて]キタサンブラック" 4'\
          f' --support "[一等星を目指して]アドマイヤベガ" 4'\
          f' --support "[届け、このオモイ！]バンブーメモリー" 4'\
          f' --support "[感謝は指先まで込めて]ファインモーション" 4'\
          f' --support "[願いまでは拭わない]ナイスネイチャ" 4'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp} --motivation 0.25'\
          f' --relation SPEED 0 {relationSpeed1}'\
          f' --relation SPEED 1 {relationSpeed2}'\
          f' --relation GUTS 0 {relationGuts1}'\
          f' --relation GUTS 1 {relationGuts2}'\
          f' --relation WISDOM 0 {relationWisdom1}'\
          f' --relation WISDOM 1 {relationWisdom2}'\
          f' --factor POWER 3 --factor POWER 3 --factor POWER 3'\
          f' --factor POWER 3 --factor POWER 3 --factor STAMINA 3'
    """

    cmd = f'java -jar ../cli/build/libs/cli.jar --count 20000 --scenario GRAND_LIVE'\
          f' --distance mile --chara "[初うらら♪さくさくら]ハルウララ" 5 5'\
          f' --support "[迫る熱に押されて]キタサンブラック" 4'\
          f' --support "[Q≠0]アグネスタキオン" 4'\
          f' --support "[感謝は指先まで込めて]ファインモーション" 4'\
          f' --support "[Dear Mr. C.B.]ミスターシービー" 4'\
          f' --support "[嗚呼華麗ナル一族]ダイイチルビー" 4'\
          f' --support "[from the GROUND UP]ライトハロー" 4'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp} --motivation 0.25'\
          f' --relation SPEED 0 {relationSpeed1}'\
          f' --relation SPEED 1 {relationSpeed2}'\
          f' --relation WISDOM 0 {relationWisdom1}'\
          f' --relation WISDOM 1 {relationWisdom2}'\
          f' --relation POWER 0 {relationPower1}'\
          f' --relation FRIEND 0 {relationFriend1}'\
          f' --factor POWER 3 --factor POWER 3 --factor POWER 3'\
          f' --factor POWER 3 --factor STAMINA 3 --factor STAMINA 3'

    print(cmd)
    score = subprocess.Popen(cmd, stdout=subprocess.PIPE, shell=True).communicate()[0]
    print(score.decode('cp932'))
    return float(score)

study = optuna.create_study(
    study_name='ls2p1w2f_1',
    storage='sqlite:///optuna_study.db',
    load_if_exists=True,
    direction='maximize'
)
study.optimize(objective, n_trials=1000)
