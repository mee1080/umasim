import subprocess
import sys
import optuna

# 使い方: uv run ./shuntaisai_optimize.py [実行時間（時間）] [評価設定（default/hint/param）]
setting = sys.argv[2] if len(sys.argv) > 2 else 'default'
index = {'default': 3, 'hint': 2, 'param': 2}[setting]
study_name = f'shuntaisai_{index}' if setting == 'default' else f'shuntaisai_{setting}_{index}'
sampler = optuna.samplers.CmaEsSampler()

# 現在のデフォルト設定（ShuntaisaiActionSelector.defaultOptions、shuntaisai_2のtrial 1315）
default_params = {
    "speed@0": 0.94,
    "stamina@0": 1.66,
    "wisdom@0": 0.5,
    "hp@0": 2.1,
    "level@0": 27.5,
    "levelUrgency@0": 169.0,

    "speed@1": 0.82,
    "stamina@1": 1.87,
    "power@1": 1.9,
    "guts@1": 1.26,
    "wisdom@1": 2.4,
    "skillPt@1": 2.1,
    "hp@1": 5.7,
    "gauge@1": 103.0,
    "level@1": 13.0,
    "levelUrgency@1": 92.0,

    "speed@2": 0.378,
    "stamina@2": 1.22,
    "power@2": 1.0,
    "guts@2": 0.84,
    "wisdom@2": 1.41,
    "skillPt@2": 2.3,
    "targetBonus@2": 0.25,
    "hp@2": 6.8,
    "gauge@2": 103.0,
    "gaugeDecayTurns@2": 3,

    "targetBonus@3": 5.0,

    "skillHint": 7.0,
}

# 現在のhintOptions（shuntaisai_hint_1の上位20試行の中央値）
hint_params = {
    "speed@0": 1.61, "stamina@0": 1.25, "power@0": 2.34, "guts@0": 2.93, "wisdom@0": 1.16,
    "hp@0": 4.3, "level@0": 14.25, "levelUrgency@0": 208.0, "hintSkill@0": 44.5,

    "speed@1": 0.57, "stamina@1": 2.14, "power@1": 0.95, "guts@1": 0.87, "wisdom@1": 1.5, "skillPt@1": 2.23,
    "hp@1": 5.45, "gauge@1": 103.0, "level@1": 12.75, "levelUrgency@1": 140.75, "hintSkill@1": 60.0,

    "speed@2": 0.441, "stamina@2": 1.285, "power@2": 1.4, "guts@2": 1.87, "wisdom@2": 1.749, "skillPt@2": 2.15,
    "targetBonus@2": 0.375, "hp@2": 7.7, "gauge@2": 109.0, "gaugeDecayTurns@2": 4, "hintSkill@2": 29.0,

    "guts@3": 3.325, "targetBonus@3": 4.5, "hintSkill@3": 128.0,

    "skillHint": 6.5,
}

# 現在のparamOptions（shuntaisai_param_1の上位20試行の中央値）
param_params = {
    "speed@0": 0.91, "stamina@0": 1.805, "power@0": 1.92, "guts@0": 3.5, "wisdom@0": 1.12,
    "hp@0": 3.7, "level@0": 11.0, "levelUrgency@0": 92.0, "hintSkill@0": 21.5,

    "speed@1": 0.49, "stamina@1": 1.12, "power@1": 1.9, "guts@1": 3.91, "wisdom@1": 1.9, "skillPt@1": 1.75,
    "hp@1": 5.2, "gauge@1": 95.5, "level@1": 25.5, "levelUrgency@1": 85.5, "hintSkill@1": 61.5,

    "speed@2": 0.594, "stamina@2": 1.175, "power@2": 1.6, "guts@2": 2.53, "wisdom@2": 1.93, "skillPt@2": 1.15,
    "targetBonus@2": 0.8, "hp@2": 7.3, "gauge@2": 106.0, "gaugeDecayTurns@2": 6, "hintSkill@2": 32.0,

    "guts@3": 1.5, "targetBonus@3": 21.25, "hintSkill@3": 45.5,

    "skillHint": 1.75,
}

initial_params = {
    'default': default_params,
    'hint': hint_params,
    'param': param_params,
}[setting]

def objective(trial):

    count = 20000

    params = {
        "speed@0": trial.suggest_float('speed@0', 0.1, 3.0, step=0.01),
        "stamina@0": trial.suggest_float('stamina@0', 0.5, 3.0, step=0.005),
        "wisdom@0": trial.suggest_float('wisdom@0', 0.1, 2.5, step=0.02),
        "hp@0": trial.suggest_float('hp@0', 1.5, 6.0, step=0.1),
        "level@0": trial.suggest_float('level@0', 0.0, 50.0, step=0.25),
        "levelUrgency@0": trial.suggest_float('levelUrgency@0', 50.0, 300.0, step=0.5),

        "speed@1": trial.suggest_float('speed@1', 0.1, 2.0, step=0.01),
        "stamina@1": trial.suggest_float('stamina@1', 0.3, 2.5, step=0.01),
        "power@1": trial.suggest_float('power@1', 0.3, 3.0, step=0.05),
        "guts@1": trial.suggest_float('guts@1', 0.3, 5.0, step=0.01),
        "wisdom@1": trial.suggest_float('wisdom@1', 0.5, 3.5, step=0.1),
        "skillPt@1": trial.suggest_float('skillPt@1', 0.5, 3.0, step=0.01),
        "hp@1": trial.suggest_float('hp@1', 1.5, 8.0, step=0.05),
        "gauge@1": trial.suggest_float('gauge@1', 20.0, 150.0, step=0.5),
        "level@1": trial.suggest_float('level@1', 0.0, 50.0, step=0.25),
        "levelUrgency@1": trial.suggest_float('levelUrgency@1', 20.0, 300.0, step=0.25),

        "speed@2": trial.suggest_float('speed@2', 0.0, 1.5, step=0.001),
        "stamina@2": trial.suggest_float('stamina@2', 0.2, 2.5, step=0.005),
        "power@2": trial.suggest_float('power@2', 0.2, 2.5, step=0.1),
        "guts@2": trial.suggest_float('guts@2', 0.2, 5.0, step=0.01),
        "wisdom@2": trial.suggest_float('wisdom@2', 0.5, 3.0, step=0.001),
        "skillPt@2": trial.suggest_float('skillPt@2', 0.5, 4.0, step=0.05),
        "targetBonus@2": trial.suggest_float('targetBonus@2', 0.0, 2.0, step=0.025),
        "hp@2": trial.suggest_float('hp@2', 1.5, 10.0, step=0.1),
        "gauge@2": trial.suggest_float('gauge@2', 30.0, 150.0, step=1.0),
        "gaugeDecayTurns@2": trial.suggest_int('gaugeDecayTurns@2', 0, 16),

        "targetBonus@3": trial.suggest_float('targetBonus@3', 0.0, 50.0, step=0.25),

        "skillHint": trial.suggest_float('skillHint', 0.0, 40.0, step=0.25),
    }
    if setting != 'default':
        params |= {
            "power@0": trial.suggest_float('power@0', 0.2, 3.0, step=0.02),
            "guts@0": trial.suggest_float('guts@0', 0.2, 5.0, step=0.01),
            "guts@3": trial.suggest_float('guts@3', 0.2, 5.0, step=0.025),
            "hintSkill@0": trial.suggest_float('hintSkill@0', 0.0, 300.0, step=0.5),
            "hintSkill@1": trial.suggest_float('hintSkill@1', 0.0, 300.0, step=0.5),
            "hintSkill@2": trial.suggest_float('hintSkill@2', 0.0, 300.0, step=0.5),
            "hintSkill@3": trial.suggest_float('hintSkill@3', 0.0, 300.0, step=0.5),
        }

    cmd = f'java -Dfile.encoding=UTF-8 -jar ../cli/build/libs/cli.jar --data-dir ../data'\
          f' --count {count} --threads 4 --setting {setting}'\
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
    study_name=study_name,
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
