import subprocess
import optuna

def objective(trial):
    speed = trial.suggest_discrete_uniform ('speed', 0.2, 2.0, 0.05)
    stamina = trial.suggest_discrete_uniform ('stamina', 0.2, 2.0, 0.05)
    power = trial.suggest_discrete_uniform ('power', 0.2, 2.0, 0.05)
    guts = trial.suggest_discrete_uniform ('guts', 0.2, 2.0, 0.05)
    wisdom = trial.suggest_discrete_uniform ('wisdom', 0.2, 2.0, 0.05)
    skillPt = trial.suggest_discrete_uniform ('skillPt', 0.1, 1.0, 0.05)
    hp = trial.suggest_discrete_uniform ('hp', 0.6, 1.5, 0.05)

    relationSpeed1 = trial.suggest_discrete_uniform ('relationSpeed1', 0.0, 20.0, 0.5)
    relationSpeed2 = trial.suggest_discrete_uniform ('relationSpeed2', 0.0, 20.0, 0.5)
#    relationSpeed3 = trial.suggest_discrete_uniform ('relationSpeed3', 0.0, 20.0, 0.5)
    relationStamina1 = trial.suggest_discrete_uniform ('relationStamina1', 0.0, 20.0, 0.5)
#    relationStamina2 = trial.suggest_discrete_uniform ('relationStamina2', 0.0, 20.0, 0.5)
#    relationStamina3 = trial.suggest_discrete_uniform ('relationStamina3', 0.0, 20.0, 0.5)
    relationPower1 = trial.suggest_discrete_uniform ('relationPower1', 0.0, 20.0, 0.5)
#    relationPower2 = trial.suggest_discrete_uniform ('relationPower2', 0.0, 20.0, 0.5)
#    relationGuts1 = trial.suggest_discrete_uniform ('relationGuts1', 0.0, 20.0, 0.5)
#    relationGuts2 = trial.suggest_discrete_uniform ('relationGuts2', 0.0, 20.0, 0.5)
#    relationGuts3 = trial.suggest_discrete_uniform ('relationGuts3', 0.0, 20.0, 0.5)
#    relationGuts4 = trial.suggest_discrete_uniform ('relationGuts4', 0.0, 20.0, 0.5)
    relationWisdom1 = trial.suggest_discrete_uniform ('relationWisdom1', 0.0, 20.0, 0.5)
#    relationWisdom2 = trial.suggest_discrete_uniform ('relationWisdom2', 0.0, 20.0, 0.5)
#    relationWisdom3 = trial.suggest_discrete_uniform ('relationWisdom3', 0.0, 20.0, 0.5)
#    relationFriend1 = trial.suggest_discrete_uniform ('relationFriend1', 0.0, 20.0, 0.5)
    relationGroup1 = trial.suggest_discrete_uniform ('relationGroup1', 0.0, 20.0, 0.5)
    passionChallenge = trial.suggest_discrete_uniform ('passionChallenge', 0.0, 20.0, 0.5)

    knowledgeSpeed = trial.suggest_discrete_uniform ('knowledgeSpeed', -10.0, 10.0, 0.5)
    knowledgeStamina = trial.suggest_discrete_uniform ('knowledgeStamina', -10.0, 10.0, 0.5)
    knowledgePower = trial.suggest_discrete_uniform ('knowledgePower', -10.0, 10.0, 0.5)
    knowledgeGuts = trial.suggest_discrete_uniform ('knowledgeGuts', -10.0, 10.0, 0.5)
    knowledgeWisdom = trial.suggest_discrete_uniform ('knowledgeWisdom', -10.0, 10.0, 0.5)
    knowledgeSkillPt = trial.suggest_discrete_uniform ('knowledgeSkillPt', -10.0, 10.0, 0.5)
    knowledgeFounder = trial.suggest_discrete_uniform ('knowledgeFounder', 0.0, 20.0, 0.5)
    knowledgeCountBase = trial.suggest_discrete_uniform ('knowledgeCountBase', 0.0, 20.0, 0.5)
    knowledgeCountFactor = trial.suggest_discrete_uniform ('knowledgeCountFactor', 0.0, 4.0, 1.0)

    """
    cmd = f'java -jar ../cli/build/libs/cli.jar --count 20000 --scenario GM'\
          f' --distance mile --chara "[初うらら♪さくさくら]ハルウララ" 5 5'\
          f' --support "[迫る熱に押されて]キタサンブラック" 4'\
          f' --support "[おセンチ注意報♪]マルゼンスキー" 4'\
          f' --support "[うらら～な休日]ハルウララ" 4'\
          f' --support "[燦爛]メジロラモーヌ" 4'\
          f' --support "[Dear Mr. C.B.]ミスターシービー" 4'\
          f' --support "[嗚呼華麗ナル一族]ダイイチルビー" 4'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp} --motivation 0.15'\
          f' --relation SPEED 0 {relationSpeed1}'\
          f' --relation SPEED 1 {relationSpeed2}'\
          f' --relation GUTS 0 {relationGuts1}'\
          f' --relation POWER 0 {relationPower1}'\
          f' --relation WISDOM 0 {relationWisdom1}'\
          f' --relation WISDOM 1 {relationWisdom2}'\
          f' --factor GUTS 3 --factor GUTS 3 --factor GUTS 3'\
          f' --factor STAMINA 3 --factor STAMINA 3 --factor STAMINA 3'\
          f' --knowledge-speed {knowledgeSpeed}'\
          f' --knowledge-stamina {knowledgeStamina}'\
          f' --knowledge-power {knowledgePower}'\
          f' --knowledge-guts {knowledgeGuts}'\
          f' --knowledge-wisdom {knowledgeWisdom}'\
          f' --knowledge-skill-pt {knowledgeSkillPt}'\
          f' --knowledge-founder {knowledgeFounder}'\
          f' --knowledge-count-base {knowledgeCountBase}'\
          f' --knowledge-count-factor {knowledgeCountFactor}'\
          f''
    """

    """
    cmd = f'java -jar ../cli/build/libs/cli.jar --count 20000 --scenario GM'\
          f' --distance mile --chara "[初うらら♪さくさくら]ハルウララ" 5 5'\
          f' --support "[迫る熱に押されて]キタサンブラック" 4'\
          f' --support "[おセンチ注意報♪]マルゼンスキー" 4'\
          f' --support "[うらら～な休日]ハルウララ" 4'\
          f' --support "[永劫続く栄光へ]祖にして導く者" 4'\
          f' --support "[Dear Mr. C.B.]ミスターシービー" 4'\
          f' --support "[嗚呼華麗ナル一族]ダイイチルビー" 4'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp} --motivation 0.15'\
          f' --relation SPEED 0 {relationSpeed1}'\
          f' --relation SPEED 1 {relationSpeed2}'\
          f' --relation GUTS 0 {relationGuts1}'\
          f' --relation POWER 0 {relationPower1}'\
          f' --relation WISDOM 0 {relationWisdom1}'\
          f' --relation GROUP 0 {relationGroup1}'\
          f' --factor GUTS 3 --factor GUTS 3 --factor GUTS 3'\
          f' --factor STAMINA 3 --factor STAMINA 3 --factor STAMINA 3'\
          f' --knowledge-speed {knowledgeSpeed}'\
          f' --knowledge-stamina {knowledgeStamina}'\
          f' --knowledge-power {knowledgePower}'\
          f' --knowledge-guts {knowledgeGuts}'\
          f' --knowledge-wisdom {knowledgeWisdom}'\
          f' --knowledge-skill-pt {knowledgeSkillPt}'\
          f' --knowledge-founder {knowledgeFounder}'\
          f' --knowledge-count-base {knowledgeCountBase}'\
          f' --knowledge-count-factor {knowledgeCountFactor}'\
          f' --passion-challenge {passionChallenge}'\
          f''
    """

    cmd = f'java -jar ../cli/build/libs/cli.jar --count 20000 --scenario GM'\
          f' --distance long --chara "[餓狼]ナリタブライアン" 5 5'\
          f' --support "[迫る熱に押されて]キタサンブラック" 4'\
          f' --support "[おセンチ注意報♪]マルゼンスキー" 4'\
          f' --support "[一粒の安らぎ]スーパークリーク" 4'\
          f' --support "[永劫続く栄光へ]祖にして導く者" 4'\
          f' --support "[Dear Mr. C.B.]ミスターシービー" 4'\
          f' --support "[パッションチャンピオーナ！]エルコンドルパサー" 4'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp} --motivation 0.15'\
          f' --relation SPEED 0 {relationSpeed1}'\
          f' --relation SPEED 1 {relationSpeed2}'\
          f' --relation STAMINA 0 {relationStamina1}'\
          f' --relation POWER 0 {relationPower1}'\
          f' --relation WISDOM 0 {relationWisdom1}'\
          f' --relation GROUP 0 {relationGroup1}'\
          f' --factor WISDOM 3 --factor WISDOM 3 --factor STAMINA 3'\
          f' --factor STAMINA 3 --factor STAMINA 3 --factor STAMINA 3'\
          f' --knowledge-speed {knowledgeSpeed}'\
          f' --knowledge-stamina {knowledgeStamina}'\
          f' --knowledge-power {knowledgePower}'\
          f' --knowledge-guts {knowledgeGuts}'\
          f' --knowledge-wisdom {knowledgeWisdom}'\
          f' --knowledge-skill-pt {knowledgeSkillPt}'\
          f' --knowledge-founder {knowledgeFounder}'\
          f' --knowledge-count-base {knowledgeCountBase}'\
          f' --knowledge-count-factor {knowledgeCountFactor}'\
          f' --passion-challenge {passionChallenge}'\
          f''

    print(cmd)
    score = subprocess.Popen(cmd, stdout=subprocess.PIPE, shell=True).communicate()[0]
    print(score.decode('cp932'))
    return float(score)

study = optuna.create_study(
    study_name='gms2h1p1w1g1_1',
    storage='sqlite:///optuna_study_gm.db',
    load_if_exists=True,
    direction='maximize'
)
study.optimize(objective, n_trials=1000)
