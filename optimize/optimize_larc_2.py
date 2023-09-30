import subprocess
import optuna

def objective(trial):
    relation = trial.suggest_float ('relation', 0.0, 20.0, step=0.5)

    """
    speed = 1.5
    stamina = 1.6
    power = 1.3
    guts = 1.3
    wisdom = 1.3
    skillPt = 0.4
    hp1 = trial.suggest_float ('hp1', 0.6, 2.0, step=0.1)
    hp2 = trial.suggest_float ('hp2', 0.6, 2.0, step=0.1)
    starGauge = trial.suggest_float ('starGauge', 0.0, 10.0, step=0.2)
    aptitudePt = trial.suggest_float ('aptitudePt', 0.0, 3.0, step=0.1)
    ssMatch = trial.suggest_float ('ssMatch1', 50.0, 150.0, step=10.0)

    cmd = f'java -jar ../cli/build/libs/cli.jar --count 50000 --scenario LARC'\
          f' --chara "[うららん一等賞♪]ハルウララ" 5 5'\
          f' --support "[大望は飛んでいく]エルコンドルパサー" 4'\
          f' --support "[The frontier]ジャングルポケット" 4'\
          f' --support "[見習い魔女と長い夜]スイープトウショウ" 4'\
          f' --support "[ハネ退け魔を退け願い込め]スペシャルウィーク" 4'\
          f' --support "[君と見る泡沫]マンハッタンカフェ" 4'\
          f' --support "[L\'aubeは迫りて]佐岳メイ" 4'\
          f' --factor STAMINA 3 --factor STAMINA 3 --factor STAMINA 3'\
          f' --factor STAMINA 3 --factor STAMINA 3 --factor STAMINA 3'\
          f' --relation NONE 0 {relation}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp1} --motivation 15.0'\
          f' --star-gauge {starGauge} --aptitude-pt {aptitudePt} --ss-match {ssMatch}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp2} --motivation 15.0'\
          f' --star-gauge {starGauge} --aptitude-pt {aptitudePt} --ss-match {ssMatch}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp1} --motivation 15.0'\
          f' --star-gauge {starGauge} --aptitude-pt {aptitudePt} --ss-match {ssMatch}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp2} --motivation 15.0'\
          f' --star-gauge {starGauge} --aptitude-pt {aptitudePt} --ss-match {ssMatch}'\
          f''
    """

    """
    speed = 1.3
    stamina = 1.3
    power = 1.2
    guts = 1.2
    wisdom = 1.0
    skillPt = 0.4
    hp1 = trial.suggest_float ('hp1', 0.6, 2.0, step=0.1)
    hp2 = trial.suggest_float ('hp2', 0.6, 2.0, step=0.1)
    motivation = trial.suggest_float ('motivation', 10.0, 20.0, step=1.0)
    starGauge = trial.suggest_float ('starGauge', 0.0, 10.0, step=0.2)
    aptitudePt = trial.suggest_float ('aptitudePt', 0.0, 3.0, step=0.1)
    ssMatch = trial.suggest_float ('ssMatch1', 100.0, 150.0, step=5.0)

    cmd = f'java -jar ../cli/build/libs/cli.jar --count 50000 --scenario LARC'\
          f' --chara "[プリンセス・オブ・ピンク]カワカミプリンセス" 5 5'\
          f' --support "[大望は飛んでいく]エルコンドルパサー" 4'\
          f' --support "[The frontier]ジャングルポケット" 4'\
          f' --support "[迫る熱に押されて]キタサンブラック" 4'\
          f' --support "[やったれハロウィンナイト！]タマモクロス" 4'\
          f' --support "[Dear Mr. C.B.]ミスターシービー" 4'\
          f' --support "[L\'aubeは迫りて]佐岳メイ" 4'\
          f' --factor STAMINA 3 --factor STAMINA 3 --factor STAMINA 3'\
          f' --factor STAMINA 3 --factor GUTS 3 --factor GUTS 3'\
          f' --relation NONE 0 {relation}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp1} --motivation {motivation}'\
          f' --star-gauge {starGauge} --aptitude-pt {aptitudePt} --ss-match {ssMatch}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp2} --motivation {motivation}'\
          f' --star-gauge {starGauge} --aptitude-pt {aptitudePt} --ss-match {ssMatch}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp1} --motivation {motivation}'\
          f' --star-gauge {starGauge} --aptitude-pt {aptitudePt} --ss-match {ssMatch}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp2} --motivation {motivation}'\
          f' --star-gauge {starGauge} --aptitude-pt {aptitudePt} --ss-match {ssMatch}'\
          f''
    """

    """
    speed = 1.3
    stamina = 1.3
    power = 1.2
    guts = 1.2
    wisdom = trial.suggest_float ('wisdom', 0.5, 1.0, step=0.05)
    skillPt = 0.4
    hp1 = trial.suggest_float ('hp1', 0.4, 2.0, step=0.1)
    hp2 = trial.suggest_float ('hp2', 0.4, 2.0, step=0.1)
    motivation = trial.suggest_float ('motivation', 10.0, 20.0, step=1.0)
    starGauge = trial.suggest_float ('starGauge', 0.0, 6.0, step=0.3)
    aptitudePt = trial.suggest_float ('aptitudePt', 0.0, 3.0, step=0.2)
    ssMatch = trial.suggest_float ('ssMatch', 100.0, 150.0, step=5.0)
    hpKeep1 = trial.suggest_float ('hpKeep1', 0.0, 5.0, step=0.2)
    hpKeep2 = trial.suggest_float ('hpKeep2', 0.0, 5.0, step=0.2)
    hpKeep3 = trial.suggest_float ('hpKeep3', 0.0, 5.0, step=0.2)
    hpKeep4 = trial.suggest_float ('hpKeep4', 0.0, 5.0, step=0.2)
    risk1 = trial.suggest_float ('risk1', 1.0, 10.0, step=0.2)
    risk2 = trial.suggest_float ('risk2', 1.0, 10.0, step=0.2)

    cmd = f'java -jar ../cli/build/libs/cli.jar --count 100000 --scenario LARC'\
          f' --chara "[プリンセス・オブ・ピンク]カワカミプリンセス" 5 5'\
          f' --support "[大望は飛んでいく]エルコンドルパサー" 4'\
          f' --support "[The frontier]ジャングルポケット" 4'\
          f' --support "[迫る熱に押されて]キタサンブラック" 4'\
          f' --support "[やったれハロウィンナイト！]タマモクロス" 4'\
          f' --support "[Dear Mr. C.B.]ミスターシービー" 4'\
          f' --support "[L\'aubeは迫りて]佐岳メイ" 4'\
          f' --factor STAMINA 3 --factor STAMINA 3 --factor STAMINA 3'\
          f' --factor STAMINA 3 --factor GUTS 3 --factor GUTS 3'\
          f' --evaluate WISDOM 0.8 1250'\
          f' --relation NONE 0 {relation}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp1} --motivation {motivation}'\
          f' --star-gauge {starGauge} --aptitude-pt {aptitudePt} --ss-match {ssMatch}'\
          f' --hp-keep {hpKeep1} --risk {risk1}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp2} --motivation {motivation}'\
          f' --star-gauge {starGauge} --aptitude-pt {aptitudePt} --ss-match {ssMatch}'\
          f' --hp-keep {hpKeep2} --risk {risk2}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp1} --motivation {motivation}'\
          f' --star-gauge {starGauge} --aptitude-pt {aptitudePt} --ss-match {ssMatch}'\
          f' --hp-keep {hpKeep3} --risk {risk1}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp2} --motivation {motivation}'\
          f' --star-gauge {starGauge} --aptitude-pt {aptitudePt} --ss-match {ssMatch}'\
          f' --hp-keep {hpKeep4} --risk {risk2}'\
          f''
    """

    speed = 1.5
    stamina = 1.7
    power = 1.0
    guts = 1.1
    wisdom = 0.9
    skillPt = 0.4
    hp1 = trial.suggest_float ('hp1', 0.4, 2.0, step=0.1)
    hp2 = trial.suggest_float ('hp2', 0.4, 2.0, step=0.1)
    motivation = trial.suggest_float ('motivation', 10.0, 20.0, step=1.0)
    starGauge = trial.suggest_float ('starGauge', 0.0, 6.0, step=0.3)
    aptitudePt = trial.suggest_float ('aptitudePt', 0.0, 3.0, step=0.2)
    ssMatch = trial.suggest_float ('ssMatch', 100.0, 150.0, step=5.0)
    hpKeep1 = trial.suggest_float ('hpKeep1', 0.0, 5.0, step=0.2)
    hpKeep2 = trial.suggest_float ('hpKeep2', 0.0, 5.0, step=0.2)
    hpKeep3 = trial.suggest_float ('hpKeep3', 0.0, 5.0, step=0.2)
    hpKeep4 = trial.suggest_float ('hpKeep4', 0.0, 5.0, step=0.2)
    risk1 = trial.suggest_float ('risk1', 1.0, 10.0, step=0.2)
    risk2 = trial.suggest_float ('risk2', 1.0, 10.0, step=0.2)

    cmd = f'java -jar ../cli/build/libs/cli.jar --count 100000 --scenario LARC'\
          f' --chara "[うららん一等賞♪]ハルウララ" 5 5'\
          f' --support "[大望は飛んでいく]エルコンドルパサー" 4'\
          f' --support "[The frontier]ジャングルポケット" 4'\
          f' --support "[迫る熱に押されて]キタサンブラック" 4'\
          f' --support "[ハネ退け魔を退け願い込め]スペシャルウィーク" 4'\
          f' --support "[君と見る泡沫]マンハッタンカフェ" 4'\
          f' --support "[L\'aubeは迫りて]佐岳メイ" 4'\
          f' --factor STAMINA 3 --factor STAMINA 3 --factor STAMINA 3'\
          f' --factor STAMINA 3 --factor STAMINA 3 --factor STAMINA 3'\
          f' --distance long'\
          f' --relation NONE 0 {relation}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp1} --motivation {motivation}'\
          f' --star-gauge {starGauge} --aptitude-pt {aptitudePt} --ss-match {ssMatch}'\
          f' --hp-keep {hpKeep1} --risk {risk1}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp2} --motivation {motivation}'\
          f' --star-gauge {starGauge} --aptitude-pt {aptitudePt} --ss-match {ssMatch}'\
          f' --hp-keep {hpKeep2} --risk {risk2}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp1} --motivation {motivation}'\
          f' --star-gauge {starGauge} --aptitude-pt {aptitudePt} --ss-match {ssMatch}'\
          f' --hp-keep {hpKeep3} --risk {risk1}'\
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp2} --motivation {motivation}'\
          f' --star-gauge {starGauge} --aptitude-pt {aptitudePt} --ss-match {ssMatch}'\
          f' --hp-keep {hpKeep4} --risk {risk2}'\
          f''

    print(cmd)
    score = subprocess.Popen(cmd, stdout=subprocess.PIPE, shell=True).communicate()[0]
    print(score.decode('cp932'))
    return float(score)

study = optuna.create_study(
    study_name='larcms3h1w1f1_2',
    storage='sqlite:///optuna_study_larc.db',
    load_if_exists=True,
    direction='maximize'
)
study.optimize(objective, n_trials=10000)
