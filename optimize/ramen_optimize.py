import subprocess
import optuna

mode = 's2h2w1'
lastCard = '[賑やかな未来を乗せて走れ！]サクラチヨノオー'
#mode = 's3h1w1'
#lastCard = '[世界を変える眼差し]アーモンドアイ'
index = 6
sampler = optuna.samplers.CmaEsSampler()

initial_params = {
    "speed1": 100,
    "wisdom1": 90,
    "hp1": 100,
    "relation1": 9000,
    "outingRelation1": 9000,
    "hpKeep1": 500,
    "risk1": 225,
    "tastingThreashold1": 500,
    "speedTastingFactor1": 120,
    "staminaTastingFactor1": 120,
    "wisdomTastingFactor1": 130,
    "tastingMinFailureRate1": 10,
    "gaugeScore1": 100,
    "gaugeMaxScore1": 7500,

    "speed2": 100,
    "wisdom2": 60,
    "hp2": 90,
    "hpKeep2": 250,
    "risk2": 175,
    "tastingThreashold2": 500,
    "allTastingFactor2": 70,
    "staminaTastingFactor2": 70,
    "wisdomTastingFactor2": 100,
    "tastingMinFailureRate2": 30,
    "gaugeScore2": 0,
    "gaugeMaxScore2": 0,

    "speed3": 100,
    "wisdom3": 75,
    "hp3": 95,
    "hpKeep3": 50,
    "risk3": 50,
    "tastingThreashold3": 600,
    "speedTastingFactor3": 100,
    "staminaTastingFactor3": 90,
    "wisdomTastingFactor3": 140,
    "gaugeScore3": 700,
    "gaugeMaxScore3": 1500,
}

def objective(trial):

    #count = 100000
    count = 50000

    status = 100
    skillPt = 200
    motivation = 1000


    speed1 = trial.suggest_int ('speed1', 40, 100, step=5)
    wisdom1 = trial.suggest_int ('wisdom1', 50, 120, step=5)
    hp1 = trial.suggest_int ('hp1', 40, 100, step=5)

    relation1 = trial.suggest_int ('relation1', 1000, 10000, step=1000)
    outingRelation1 = trial.suggest_int ('outingRelation1', 1000, 10000, step=1000)
    hpKeep1 = trial.suggest_int ('hpKeep1', 50, 500, step=50)
    risk1 = trial.suggest_int ('risk1', 50, 250, step=25)

    tastingThreashold1 = trial.suggest_int ('tastingThreashold1', 500, 900, step=100)
    allTastingFactor1 = 0
    speedTastingFactor1 = trial.suggest_int ('speedTastingFactor1', 50, 150, step=10)
    staminaTastingFactor1 = trial.suggest_int ('staminaTastingFactor1', 50, 150, step=10)
    wisdomTastingFactor1 = trial.suggest_int ('wisdomTastingFactor1', 50, 150, step=10)
    tastingMinFailureRate1 = trial.suggest_int ('tastingMinFailureRate1', 0, 30, step=10)
    gaugeScore1 = trial.suggest_int ('gaugeScore1', 0, 2000, step=100)
    gaugeMaxScore1 = trial.suggest_int ('gaugeMaxScore1', 0, 10000, step=500)


    speed2 = trial.suggest_int ('speed2', 40, 100, step=5)
    wisdom2 = trial.suggest_int ('wisdom2', 50, 120, step=5)
    hp2 = trial.suggest_int ('hp2', 40, 100, step=5)

    relation2 = 5000
    outingRelation2 = 5000
    hpKeep2 = trial.suggest_int ('hpKeep2', 50, 500, step=50)
    risk2 = trial.suggest_int ('risk2', 50, 250, step=25)

    tastingThreashold2 = trial.suggest_int ('tastingThreashold2', 500, 900, step=100)
    allTastingFactor2 = trial.suggest_int ('allTastingFactor2', 50, 150, step=10)
    speedTastingFactor2 = 0
    staminaTastingFactor2 = trial.suggest_int ('staminaTastingFactor2', 50, 150, step=10)
    wisdomTastingFactor2 = trial.suggest_int ('wisdomTastingFactor2', 50, 150, step=10)
    tastingMinFailureRate2 = trial.suggest_int ('tastingMinFailureRate2', 0, 30, step=10)
    gaugeScore2 = trial.suggest_int ('gaugeScore2', 0, 2000, step=100)
    gaugeMaxScore2 = trial.suggest_int ('gaugeMaxScore2', 0, 10000, step=500)


    speed3 = trial.suggest_int ('speed3', 40, 100, step=5)
    wisdom3 = trial.suggest_int ('wisdom3', 50, 120, step=5)
    hp3 = trial.suggest_int ('hp3', 40, 100, step=5)

    relation3 = 5000
    outingRelation3 = 5000
    hpKeep3 = trial.suggest_int ('hpKeep3', 50, 500, step=50)
    risk3 = trial.suggest_int ('risk3', 50, 250, step=25)

    tastingThreashold3 = trial.suggest_int ('tastingThreashold3', 500, 900, step=100)
    allTastingFactor3 = 0
    speedTastingFactor3 = trial.suggest_int ('speedTastingFactor3', 50, 150, step=10)
    staminaTastingFactor3 = trial.suggest_int ('staminaTastingFactor3', 50, 150, step=10)
    wisdomTastingFactor3 = trial.suggest_int ('wisdomTastingFactor3', 50, 150, step=10)
    tastingMinFailureRate3 = 100
    gaugeScore3 = trial.suggest_int ('gaugeScore3', 0, 2000, step=100)
    gaugeMaxScore3 = trial.suggest_int ('gaugeMaxScore3', 0, 10000, step=500)


    cmd = f'java -Dfile.encoding=UTF-8 -jar ../cli/build/libs/cli.jar --data-dir ../data'\
          f' --count {count}'\
          f' --evaluate {mode}'\
          f' --chara "[初うらら♪さくさくら]ハルウララ" 5 5'\
          f' --support "[天才的ユートピア]トウカイテイオー" 4'\
          f' --support "[心覚えし、京の華]エアグルーヴ" 4'\
          f' --support "[その執念は怒濤が如く]メイショウドトウ" 4'\
          f' --support "{lastCard}" 4'\
          f' --support "[Innovator]フォーエバーヤング" 4' \
          f' --support "[一杯のノスタルジア]駿川たづな" 4' \
          f' --factor SPEED 3 --factor SPEED 3 --factor SPEED 3'\
          f' --factor SPEED 3 --factor SPEED 3 --factor SPEED 3'\
          \
          f' --status {status} --speed {speed1} --wisdom {wisdom1} --skill-pt {skillPt}'\
          f' --hp {hp1} --motivation {motivation}'\
          f' --relation {relation1} --outing-relation {outingRelation1}'\
          f' --hp-keep {hpKeep1} --risk {risk1}'\
          f' --tasting-threashold {tastingThreashold1} --all-tasting-factor {allTastingFactor1}'\
          f' --speed-tasting-factor {speedTastingFactor1} --stamina-tasting-factor {staminaTastingFactor1}'\
          f' --wisdom-tasting-factor {wisdomTastingFactor1} --tasting-min-failure-rate {tastingMinFailureRate1}'\
          f' --gauge-score {gaugeScore1} --gauge-max-score {gaugeMaxScore1}'\
          \
          f' --status {status} --speed {speed2} --wisdom {wisdom2} --skill-pt {skillPt}'\
          f' --hp {hp2} --motivation {motivation}'\
          f' --relation {relation2} --outing-relation {outingRelation2}'\
          f' --hp-keep {hpKeep2} --risk {risk2}'\
          f' --tasting-threashold {tastingThreashold2} --all-tasting-factor {allTastingFactor2}'\
          f' --speed-tasting-factor {speedTastingFactor2} --stamina-tasting-factor {staminaTastingFactor2}'\
          f' --wisdom-tasting-factor {wisdomTastingFactor2} --tasting-min-failure-rate {tastingMinFailureRate2}'\
          f' --gauge-score {gaugeScore2} --gauge-max-score {gaugeMaxScore2}'\
          \
          f' --status {status} --speed {speed3} --wisdom {wisdom3} --skill-pt {skillPt}'\
          f' --hp {hp3} --motivation {motivation}'\
          f' --relation {relation3} --outing-relation {outingRelation3}'\
          f' --hp-keep {hpKeep3} --risk {risk3}'\
          f' --tasting-threashold {tastingThreashold3} --all-tasting-factor {allTastingFactor3}'\
          f' --speed-tasting-factor {speedTastingFactor3} --stamina-tasting-factor {staminaTastingFactor3}'\
          f' --wisdom-tasting-factor {wisdomTastingFactor3} --tasting-min-failure-rate {tastingMinFailureRate3}'\
          f' --gauge-score {gaugeScore3} --gauge-max-score {gaugeMaxScore3}'\
          \
          f' --status 0 --wisdom 0 --skill-pt 1000'\
          f' --hp 0 --motivation 0'\
          f' --relation 0 --outing-relation 0'\
          f' --hp-keep 0 --risk 0'\
          \
          f''

    print(cmd)
    score = subprocess.Popen(cmd, stdout=subprocess.PIPE, shell=True).communicate()[0]
    print(score.decode('cp932'))
    return float(score)

storage = optuna.storages.RDBStorage(
    url="sqlite:///optuna_study_ramen.db",
    engine_kwargs={"connect_args": {"timeout": 60}}
)

study = optuna.create_study(
    sampler=sampler,
    study_name=f'ramen_{mode}_{index}',
    storage=storage,
    load_if_exists=True,
    direction='maximize'
)
if len(study.get_trials(deepcopy=False)) == 0:
    print('enque initial trial')
    study.enqueue_trial(initial_params)

study.optimize(objective, n_trials=100000)
