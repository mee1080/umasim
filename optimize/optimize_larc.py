import subprocess
import optuna

def objective(trial):
    speed = trial.suggest_float ('speed', 0.2, 2.0, step=0.05)
    stamina = trial.suggest_float ('stamina', 0.2, 2.0, step=0.05)
    power = trial.suggest_float ('power', 0.2, 2.0, step=0.05)
    guts = trial.suggest_float ('guts', 0.2, 2.0, step=0.05)
    wisdom = trial.suggest_float ('wisdom', 0.2, 2.0, step=0.05)
    skillPt = trial.suggest_float ('skillPt', 0.1, 1.0, step=0.05)
    hp = trial.suggest_float ('hp', 0.6, 1.5, step=0.05)

    relation = trial.suggest_float ('relation', 0.0, 20.0, step=0.5)

    starGauge = trial.suggest_float ('starGauge', 0.0, 20.0, step=0.5)
    aptitudePt = trial.suggest_float ('aptitudePt', 0.0, 20.0, step=0.5)
    ssMatch = trial.suggest_float ('ssMatch', 50.0, 150.0, step=10.0)

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
          f' --speed {speed} --stamina {stamina} --power {power} --guts {guts}'\
          f' --wisdom {wisdom} --skill-pt {skillPt} --hp {hp} --motivation 15.0'\
          f' --relation NONE 0 {relation}'\
          f' --star-gauge {starGauge}'\
          f' --aptitude-pt {aptitudePt}'\
          f' --ss-match {ssMatch}'\
          f''

    print(cmd)
    score = subprocess.Popen(cmd, stdout=subprocess.PIPE, shell=True).communicate()[0]
    print(score.decode('cp932'))
    return float(score)

study = optuna.create_study(
    study_name='larcls3h1w1f1_1',
    storage='sqlite:///optuna_study_larc.db',
    load_if_exists=True,
    direction='maximize'
)
study.optimize(objective, n_trials=1000)
