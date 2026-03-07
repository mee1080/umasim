import subprocess
import optuna

def objective(trial):

    count = 100000

    status = 100
    skillPt = 1000
    motivation = 1000


    wisdom1 = trial.suggest_int ('wisdom1', 50, 100, step=5)
    hp1 = trial.suggest_int ('hp1', 400, 1000, step=50)

    relation1 = trial.suggest_int ('relation', 6000, 30000, step=2000)
    outingRelation1 = trial.suggest_int ('outingRelation', 6000, 30000, step=2000)
    wisdomRelation1 = trial.suggest_int ('wisdomRelation', 6000, 30000, step=2000)
    hpKeep1 = trial.suggest_int ('hpKeep1', 600, 1500, step=100)
    risk1 = trial.suggest_int ('risk1', 100, 500, step=50)

    dreamGauge11 = trial.suggest_int ('dreamGauge11', 8000, 30000, step=2000)
    dreamGauge21 = 1000
    dreamGauge31 = 1000
    dreamGaugeMax1 = trial.suggest_int ('dreamGaugeMax1', -10000, 10000, step=2000)


    wisdom2 = trial.suggest_int ('wisdom2', 50, 100, step=5)
    hp2 = trial.suggest_int ('hp2', 400, 1000, step=50)

    relation2 = relation1
    outingRelation2 = outingRelation1
    wisdomRelation2 = wisdomRelation1
    hpKeep2 = trial.suggest_int ('hpKeep2', 600, 1500, step=100)
    risk2 = trial.suggest_int ('risk2', 100, 500, step=50)

    dreamGauge12 = trial.suggest_int ('dreamGauge12', 8000, 30000, step=2000)
    dreamGauge22 = 1000
    dreamGauge32 = 1000
    dreamGaugeMax2 = trial.suggest_int ('dreamGaugeMax2', -10000, 10000, step=2000)


    wisdom3 = trial.suggest_int ('wisdom3', 50, 100, step=5)
    hp3 = trial.suggest_int ('hp3', 400, 1000, step=50)

    relation3 = relation1
    outingRelation3 = outingRelation1
    wisdomRelation3 = wisdomRelation1
    hpKeep3 = trial.suggest_int ('hpKeep3', 600, 1500, step=100)
    risk3 = trial.suggest_int ('risk3', 100, 500, step=50)

    dreamGauge13 = trial.suggest_int ('dreamGauge13', 8000, 30000, step=2000)
    dreamGauge23 = 1000
    dreamGauge33 = 1000
    dreamGaugeMax3 = trial.suggest_int ('dreamGaugeMax3', -10000, 10000, step=2000)


    wisdom4 = trial.suggest_int ('wisdom4', 50, 100, step=5)
    hp4 = trial.suggest_int ('hp4', 400, 1000, step=50)

    relation4 = 10000
    outingRelation4 = outingRelation1
    wisdomRelation4 = wisdomRelation1
    hpKeep4 = trial.suggest_int ('hpKeep4', 600, 1500, step=100)
    risk4 = trial.suggest_int ('risk4', 100, 500, step=50)

    dreamGauge14 = trial.suggest_int ('dreamGauge14', 8000, 30000, step=2000)
    dreamGauge24 = 1000
    dreamGauge34 = 1000
    dreamGaugeMax4 = trial.suggest_int ('dreamGaugeMax4', -10000, 10000, step=2000)

    cmd = f'java -Dfile.encoding=UTF-8 -jar ../cli/build/libs/cli.jar --data-dir ../data'\
          f' --count {count}'\
          f' --chara "[初うらら♪さくさくら]ハルウララ" 5 5'\
          f' --support "[天才的ユートピア]トウカイテイオー" 4'\
          f' --support "[心覚えし、京の華]エアグルーヴ" 4'\
          f' --support "[ぬくもりのノエル]フェノーメノ" 4'\
          f' --support "[星跨ぐメッセージ]ネオユニヴァース" 4'\
          f' --support "[Innovator]フォーエバーヤング" 4' \
          f' --support "[American Dream]カジノドライヴ" 4' \
          f' --factor SPEED 3 --factor SPEED 3 --factor SPEED 3'\
          f' --factor SPEED 3 --factor SPEED 3 --factor SPEED 3'\
          f' --route Classic'\
          \
          f' --status {status} --wisdom {wisdom1} --skill-pt {skillPt}'\
          f' --hp {hp1} --motivation {motivation}'\
          f' --relation {relation1} --relation {outingRelation1} --wisdom-relation {wisdomRelation1}'\
          f' --hp-keep {hpKeep1} --risk {risk1}'\
          f' --dream-gauge1 {dreamGauge11} --dream-gauge2 {dreamGauge21} --dream-gauge3 {dreamGauge31}'\
          f' --dream-gauge-max {dreamGaugeMax1}'\
          \
          f' --status {status} --wisdom {wisdom2} --skill-pt {skillPt}'\
          f' --hp {hp2} --motivation {motivation}'\
          f' --relation {relation2} --relation {outingRelation2} --wisdom-relation {wisdomRelation2}'\
          f' --hp-keep {hpKeep2} --risk {risk2}'\
          f' --dream-gauge1 {dreamGauge12} --dream-gauge2 {dreamGauge22} --dream-gauge3 {dreamGauge32}'\
          f' --dream-gauge-max {dreamGaugeMax2}'\
          \
          f' --status {status} --wisdom {wisdom3} --skill-pt {skillPt}'\
          f' --hp {hp3} --motivation {motivation}'\
          f' --relation {relation3} --relation {outingRelation3} --wisdom-relation {wisdomRelation3}'\
          f' --hp-keep {hpKeep3} --risk {risk3}'\
          f' --dream-gauge1 {dreamGauge13} --dream-gauge2 {dreamGauge23} --dream-gauge3 {dreamGauge33}'\
          f' --dream-gauge-max {dreamGaugeMax3}'\
          \
          f' --status {status} --wisdom {wisdom4} --skill-pt {skillPt}'\
          f' --hp {hp4} --motivation {motivation}'\
          f' --relation {relation4} --relation {outingRelation4} --wisdom-relation {wisdomRelation4}'\
          f' --hp-keep {hpKeep4} --risk {risk4}'\
          f' --dream-gauge1 {dreamGauge14} --dream-gauge2 {dreamGauge24} --dream-gauge3 {dreamGauge34}'\
          f' --dream-gauge-max {dreamGaugeMax4}'\
          f''

    print(cmd)
    score = subprocess.Popen(cmd, stdout=subprocess.PIPE, shell=True).communicate()[0]
    print(score.decode('cp932'))
    return float(score)

study = optuna.create_study(
    study_name='bc_s2h1p1w1_3',
    storage='sqlite:///optuna_study_bc.db',
    load_if_exists=True,
    direction='maximize'
)
study.optimize(objective, n_trials=100000)
