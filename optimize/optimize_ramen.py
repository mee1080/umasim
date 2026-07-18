import subprocess
import optuna

def objective(trial):

    #count = 100000
    count = 10000

    status = 100
    skillPt = 200
    motivation = 1000


    wisdom1 = trial.suggest_int ('wisdom1', 50, 120, step=5)
    hp1 = trial.suggest_int ('hp1', 40, 100, step=5)

    relation1 = trial.suggest_int ('relation1', 1000, 10000, step=1000)
    outingRelation1 = trial.suggest_int ('outingRelation1', 1000, 10000, step=1000)
    hpKeep1 = trial.suggest_int ('hpKeep1', 50, 500, step=50)
    risk1 = trial.suggest_int ('risk1', 50, 250, step=25)

    tastingThreashold1 = trial.suggest_int ('tastingThreashold1', 500, 900, step=100)
    allTastingFactor1 = 100
    speedTastingFactor1 = trial.suggest_int ('tastingFactor1', 50, 150, step=10)
    staminaTastingFactor1 = speedTastingFactor1
    wisdomTastingFactor1 = trial.suggest_int ('wisdomTastingFactor1', 50, 150, step=10)
    tastingMinFailureRate1 = trial.suggest_int ('tastingMinFailureRate1', 0, 30, step=10)
    gaugeScore1 = trial.suggest_int ('gaugeScore1', 0, 2000, step=100)
    gaugeMaxScore1 = trial.suggest_int ('gaugeMaxScore1', 0, 10000, step=500)


    wisdom2 = trial.suggest_int ('wisdom2', 50, 120, step=5)
    hp2 = trial.suggest_int ('hp2', 40, 100, step=5)

    relation2 = 5000
    outingRelation2 = 5000
    hpKeep2 = trial.suggest_int ('hpKeep2', 50, 500, step=50)
    risk2 = trial.suggest_int ('risk2', 50, 250, step=25)

    tastingThreashold2 = trial.suggest_int ('tastingThreashold2', 500, 900, step=100)
    allTastingFactor2 = trial.suggest_int ('allTastingFactor2', 50, 150, step=10)
    speedTastingFactor2 = 100
    staminaTastingFactor2 = trial.suggest_int ('staminaTastingFactor2', 50, 150, step=10)
    wisdomTastingFactor2 = trial.suggest_int ('wisdomTastingFactor2', 50, 150, step=10)
    tastingMinFailureRate2 = trial.suggest_int ('tastingMinFailureRate2', 0, 30, step=10)
    gaugeScore2 = trial.suggest_int ('gaugeScore2', 0, 2000, step=100)
    gaugeMaxScore2 = trial.suggest_int ('gaugeMaxScore2', 0, 10000, step=500)


    wisdom3 = trial.suggest_int ('wisdom3', 50, 120, step=5)
    hp3 = trial.suggest_int ('hp3', 40, 100, step=5)

    relation3 = 5000
    outingRelation3 = 5000
    hpKeep3 = trial.suggest_int ('hpKeep3', 50, 500, step=50)
    risk3 = trial.suggest_int ('risk3', 50, 250, step=25)

    tastingThreashold3 = trial.suggest_int ('tastingThreashold3', 500, 900, step=100)
    allTastingFactor3 = 100
    speedTastingFactor3 = trial.suggest_int ('speedTastingFactor3', 50, 150, step=10)
    staminaTastingFactor3 = trial.suggest_int ('staminaTastingFactor3', 50, 150, step=10)
    wisdomTastingFactor3 = trial.suggest_int ('wisdomTastingFactor3', 50, 150, step=10)
    tastingMinFailureRate3 = 100
    gaugeScore3 = trial.suggest_int ('gaugeScore3', 0, 2000, step=100)
    gaugeMaxScore3 = trial.suggest_int ('gaugeMaxScore3', 0, 10000, step=500)


    cmd = f'java -Dfile.encoding=UTF-8 -jar ../cli/build/libs/cli.jar --data-dir ../data'\
          f' --count {count}'\
          f' --chara "[初うらら♪さくさくら]ハルウララ" 5 5'\
          f' --support "[天才的ユートピア]トウカイテイオー" 4'\
          f' --support "[心覚えし、京の華]エアグルーヴ" 4'\
          f' --support "[その執念は怒濤が如く]メイショウドトウ" 4'\
          f' --support "[賑やかな未来を乗せて走れ！]サクラチヨノオー" 4'\
          f' --support "[Innovator]フォーエバーヤング" 4' \
          f' --support "[一杯のノスタルジア]駿川たづな" 4' \
          f' --factor SPEED 3 --factor SPEED 3 --factor SPEED 3'\
          f' --factor SPEED 3 --factor SPEED 3 --factor SPEED 3'\
          \
          f' --status {status} --wisdom {wisdom1} --skill-pt {skillPt}'\
          f' --hp {hp1} --motivation {motivation}'\
          f' --relation {relation1} --outing-relation {outingRelation1}'\
          f' --hp-keep {hpKeep1} --risk {risk1}'\
          f' --tasting-threashold {tastingThreashold1} --all-tasting-factor {allTastingFactor1}'\
          f' --speed-tasting-factor {speedTastingFactor1} --stamina-tasting-factor {staminaTastingFactor1}'\
          f' --wisdom-tasting-factor {wisdomTastingFactor1} --tasting-min-failure-rate {tastingMinFailureRate1}'\
          f' --gauge-score {gaugeScore1} --gauge-max-score {gaugeMaxScore1}'\
          \
          f' --status {status} --wisdom {wisdom2} --skill-pt {skillPt}'\
          f' --hp {hp2} --motivation {motivation}'\
          f' --relation {relation2} --outing-relation {outingRelation2}'\
          f' --hp-keep {hpKeep2} --risk {risk2}'\
          f' --tasting-threashold {tastingThreashold2} --all-tasting-factor {allTastingFactor2}'\
          f' --speed-tasting-factor {speedTastingFactor2} --stamina-tasting-factor {staminaTastingFactor2}'\
          f' --wisdom-tasting-factor {wisdomTastingFactor2} --tasting-min-failure-rate {tastingMinFailureRate2}'\
          f' --gauge-score {gaugeScore2} --gauge-max-score {gaugeMaxScore2}'\
          \
          f' --status {status} --wisdom {wisdom3} --skill-pt {skillPt}'\
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

study = optuna.create_study(
    study_name='ramen_s2h2w1_1',
    storage='sqlite:///optuna_study_ramen.db',
    load_if_exists=True,
    direction='maximize'
)
study.optimize(objective, n_trials=1000000)
