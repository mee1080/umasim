import subprocess
import sys
import optuna

index = 2
sampler = optuna.samplers.CmaEsSampler()

# 現在のデフォルト設定（ShuntaisaiActionSelector.defaultOptions、shuntaisai_1のtrial 548）
initial_params = {
    "speed@0": 0.68,
    "stamina@0": 1.25,
    "wisdom@0": 0.54,
    "hp@0": 3.3,
    "level@0": 39.0,
    "levelUrgency@0": 122.0,

    "speed@1": 0.5,
    "stamina@1": 1.58,
    "power@1": 1.7,
    "guts@1": 1.42,
    "wisdom@1": 2.8,
    "skillPt@1": 2.47,
    "hp@1": 5.0,
    "gauge@1": 106.0,
    "level@1": 21.0,
    "levelUrgency@1": 135.0,

    "speed@2": 0.476,
    "stamina@2": 0.81,
    "power@2": 0.7,
    "guts@2": 0.78,
    "wisdom@2": 1.0,
    "skillPt@2": 1.8,
    "targetBonus@2": 0.4,
    "hp@2": 5.8,
    "gauge@2": 81.0,
    "gaugeDecayTurns@2": 4,

    "targetBonus@3": 12.5,

    "skillHint": 6.0,
}

def objective(trial):

    count = 20000

    params = {
        "speed@0": trial.suggest_float('speed@0', 0.1, 3.0, step=0.02),
        "stamina@0": trial.suggest_float('stamina@0', 0.5, 3.0, step=0.01),
        "wisdom@0": trial.suggest_float('wisdom@0', 0.1, 2.5, step=0.02),
        "hp@0": trial.suggest_float('hp@0', 1.5, 6.0, step=0.1),
        "level@0": trial.suggest_float('level@0', 0.0, 50.0, step=0.5),
        "levelUrgency@0": trial.suggest_float('levelUrgency@0', 50.0, 300.0, step=0.5),

        "speed@1": trial.suggest_float('speed@1', 0.3, 2.0, step=0.02),
        "stamina@1": trial.suggest_float('stamina@1', 0.3, 2.5, step=0.01),
        "power@1": trial.suggest_float('power@1', 0.3, 2.0, step=0.1),
        "guts@1": trial.suggest_float('guts@1', 0.3, 2.0, step=0.02),
        "wisdom@1": trial.suggest_float('wisdom@1', 0.5, 3.5, step=0.1),
        "skillPt@1": trial.suggest_float('skillPt@1', 0.5, 3.0, step=0.01),
        "hp@1": trial.suggest_float('hp@1', 1.5, 6.0, step=0.1),
        "gauge@1": trial.suggest_float('gauge@1', 20.0, 120.0, step=1.0),
        "level@1": trial.suggest_float('level@1', 0.0, 50.0, step=0.5),
        "levelUrgency@1": trial.suggest_float('levelUrgency@1', 20.0, 300.0, step=0.5),

        "speed@2": trial.suggest_float('speed@2', 0.0, 1.5, step=0.002),
        "stamina@2": trial.suggest_float('stamina@2', 0.2, 1.5, step=0.005),
        "power@2": trial.suggest_float('power@2', 0.2, 1.5, step=0.1),
        "guts@2": trial.suggest_float('guts@2', 0.2, 2.0, step=0.02),
        "wisdom@2": trial.suggest_float('wisdom@2', 0.5, 3.0, step=0.002),
        "skillPt@2": trial.suggest_float('skillPt@2', 1.0, 4.0, step=0.1),
        "targetBonus@2": trial.suggest_float('targetBonus@2', 0.0, 2.0, step=0.05),
        "hp@2": trial.suggest_float('hp@2', 1.5, 8.0, step=0.1),
        "gauge@2": trial.suggest_float('gauge@2', 30.0, 150.0, step=1.0),
        "gaugeDecayTurns@2": trial.suggest_int('gaugeDecayTurns@2', 0, 16),

        "targetBonus@3": trial.suggest_float('targetBonus@3', 5.0, 50.0, step=0.5),

        "skillHint": trial.suggest_float('skillHint', 0.0, 40.0, step=0.5),
    }

    cmd = f'java -Dfile.encoding=UTF-8 -jar ../cli/build/libs/cli.jar --data-dir ../data'\
          f' --count {count} --threads 4'\
          f' --chara "[初うらら♪さくさくら]ハルウララ" 5 5'\
          f' --support "[時に交わる海と空]ミスターシービー" 4'\
          f' --support "[As if Guided]エフフォーリア" 4'\
          f' --support "[その執念は怒濤が如く]メイショウドトウ" 4'\
          f' --support "[Time of Departure]マヤノトップガン" 4'\
          f' --support "[Innovator]フォーエバーヤング" 4'\
          f' --support "[いとどめでたき、まつりかな]吾妻" 4'\
          f' --factor GUTS 3 --factor GUTS 3 --factor GUTS 3'\
          f' --factor WISDOM 3 --factor WISDOM 3 --factor WISDOM 3'\
          + ''.join(f' --param {name}={value}' for name, value in params.items())

    print(cmd)
    score = subprocess.Popen(cmd, stdout=subprocess.PIPE, shell=True).communicate()[0]
    print(score.decode('cp932'))
    return float(score)

storage = optuna.storages.RDBStorage(
    url="sqlite:///optuna_study_shuntaisai.db",
    engine_kwargs={"connect_args": {"timeout": 60}}
)

study = optuna.create_study(
    sampler=sampler,
    study_name=f'shuntaisai_{index}',
    storage=storage,
    load_if_exists=True,
    direction='maximize'
)
if len(study.get_trials(deepcopy=False)) == 0:
    print('enque initial trial')
    study.enqueue_trial(initial_params)

# 引数で実行時間（時間）を指定可能
timeout = float(sys.argv[1]) * 3600 if len(sys.argv) > 1 else None
study.optimize(objective, n_trials=100000, timeout=timeout)
