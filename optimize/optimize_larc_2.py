import subprocess
import optuna

def objective(trial):
    relation = trial.suggest_float ('relation', 0.0, 20.0, step=0.5)

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

    print(cmd)
    score = subprocess.Popen(cmd, stdout=subprocess.PIPE, shell=True).communicate()[0]
    print(score.decode('cp932'))
    return float(score)

study = optuna.create_study(
    study_name='larcls3h1w1f1_6',
    storage='sqlite:///optuna_study_larc.db',
    load_if_exists=True,
    direction='maximize'
)
study.optimize(objective, n_trials=10000)
