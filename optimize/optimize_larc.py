import subprocess
import optuna

def objective(trial):
    relation = trial.suggest_float ('relation', 0.0, 20.0, step=0.5)

    speed1 = trial.suggest_float ('speed1', 0.2, 2.0, step=0.05)
    stamina1 = trial.suggest_float ('stamina1', 0.2, 2.0, step=0.05)
    power1 = trial.suggest_float ('power1', 0.2, 2.0, step=0.05)
    guts1 = trial.suggest_float ('guts1', 0.2, 2.0, step=0.05)
    wisdom1 = trial.suggest_float ('wisdom1', 0.2, 2.0, step=0.05)
    skillPt1 = trial.suggest_float ('skillPt1', 0.1, 1.0, step=0.05)
    hp1 = trial.suggest_float ('hp1', 0.6, 1.5, step=0.05)
    starGauge1 = trial.suggest_float ('starGauge1', 0.0, 20.0, step=0.5)
    aptitudePt1 = 0.0
    ssMatch1 = trial.suggest_float ('ssMatch1', 50.0, 150.0, step=10.0)

    speed2 = trial.suggest_float ('speed2', 0.2, 2.0, step=0.05)
    stamina2 = trial.suggest_float ('stamina2', 0.2, 2.0, step=0.05)
    power2 = trial.suggest_float ('power2', 0.2, 2.0, step=0.05)
    guts2 = trial.suggest_float ('guts2', 0.2, 2.0, step=0.05)
    wisdom2 = trial.suggest_float ('wisdom2', 0.2, 2.0, step=0.05)
    skillPt2 = trial.suggest_float ('skillPt2', 0.1, 1.0, step=0.05)
    hp2 = trial.suggest_float ('hp2', 0.6, 1.5, step=0.05)
    starGauge2 = 0.0
    aptitudePt2 = trial.suggest_float ('aptitudePt2', 0.0, 20.0, step=0.5)
    ssMatch2 = 0.0

    speed3 = trial.suggest_float ('speed3', 0.2, 2.0, step=0.05)
    stamina3 = trial.suggest_float ('stamina3', 0.2, 2.0, step=0.05)
    power3 = trial.suggest_float ('power3', 0.2, 2.0, step=0.05)
    guts3 = trial.suggest_float ('guts3', 0.2, 2.0, step=0.05)
    wisdom3 = trial.suggest_float ('wisdom3', 0.2, 2.0, step=0.05)
    skillPt3 = trial.suggest_float ('skillPt3', 0.1, 1.0, step=0.05)
    hp3 = trial.suggest_float ('hp3', 0.6, 1.5, step=0.05)
    starGauge3 = trial.suggest_float ('starGauge3', 0.0, 20.0, step=0.5)
    aptitudePt3 = 0.0
    ssMatch3 = trial.suggest_float ('ssMatch3', 50.0, 150.0, step=10.0)

    speed4 = trial.suggest_float ('speed4', 0.2, 2.0, step=0.05)
    stamina4 = trial.suggest_float ('stamina4', 0.2, 2.0, step=0.05)
    power4 = trial.suggest_float ('power4', 0.2, 2.0, step=0.05)
    guts4 = trial.suggest_float ('guts4', 0.2, 2.0, step=0.05)
    wisdom4 = trial.suggest_float ('wisdom4', 0.2, 2.0, step=0.05)
    skillPt4 = trial.suggest_float ('skillPt4', 0.1, 1.0, step=0.05)
    hp4 = trial.suggest_float ('hp4', 0.6, 1.5, step=0.05)
    starGauge4 = 0.0
    aptitudePt4 = trial.suggest_float ('aptitudePt4', 0.0, 20.0, step=0.5)
    ssMatch4 = 0.0

    cmd = f'java -jar ../cli/build/libs/cli.jar --count 20000 --scenario LARC'\
          f' --distance long --chara "[うららん一等賞♪]ハルウララ" 5 5'\
          f' --support "[大望は飛んでいく]エルコンドルパサー" 4'\
          f' --support "[The frontier]ジャングルポケット" 4'\
          f' --support "[見習い魔女と長い夜]スイープトウショウ" 4'\
          f' --support "[ハネ退け魔を退け願い込め]スペシャルウィーク" 4'\
          f' --support "[君と見る泡沫]マンハッタンカフェ" 4'\
          f' --support "[L\'aubeは迫りて]佐岳メイ" 4'\
          f' --factor STAMINA 3 --factor STAMINA 3 --factor STAMINA 3'\
          f' --factor STAMINA 3 --factor STAMINA 3 --factor WISDOM 3'\
          f' --relation NONE 0 {relation}'\
          f' --speed {speed1} --stamina {stamina1} --power {power1} --guts {guts1}'\
          f' --wisdom {wisdom1} --skill-pt {skillPt1} --hp {hp1} --motivation 15.0'\
          f' --star-gauge {starGauge1} --aptitude-pt {aptitudePt1} --ss-match {ssMatch1}'\
          f' --speed {speed2} --stamina {stamina2} --power {power2} --guts {guts2}'\
          f' --wisdom {wisdom2} --skill-pt {skillPt2} --hp {hp2} --motivation 15.0'\
          f' --star-gauge {starGauge2} --aptitude-pt {aptitudePt2} --ss-match {ssMatch2}'\
          f' --speed {speed3} --stamina {stamina3} --power {power3} --guts {guts3}'\
          f' --wisdom {wisdom3} --skill-pt {skillPt3} --hp {hp3} --motivation 15.0'\
          f' --star-gauge {starGauge3} --aptitude-pt {aptitudePt3} --ss-match {ssMatch3}'\
          f' --speed {speed4} --stamina {stamina4} --power {power4} --guts {guts4}'\
          f' --wisdom {wisdom4} --skill-pt {skillPt4} --hp {hp4} --motivation 15.0'\
          f' --star-gauge {starGauge4} --aptitude-pt {aptitudePt4} --ss-match {ssMatch4}'\
          f''

    print(cmd)
    score = subprocess.Popen(cmd, stdout=subprocess.PIPE, shell=True).communicate()[0]
    print(score.decode('cp932'))
    return float(score)

study = optuna.create_study(
    study_name='larcls3h1w1f1_2',
    storage='sqlite:///optuna_study_larc.db',
    load_if_exists=True,
    direction='maximize'
)
study.optimize(objective, n_trials=1000)
