import logging
import os
import yaml

logger = logging.getLogger('__main__')


def merge_dicts(dict1, dict2):
    """递归合并两个字典，dict2覆盖dict1"""
    for key, value in dict2.items():
        if key in dict1 and isinstance(dict1[key], dict) and isinstance(value, dict):
            merge_dicts(dict1[key], value)
        else:
            dict1[key] = value
    return dict1


def load_config(env):
    # 加载默认配置，允许config.yml为空
    default_config = {}
    if os.path.exists('config.yml'):
        with open('config.yml', 'r', encoding='utf-8') as f:
            raw = yaml.safe_load(f)
            # yaml空文件会返回None，转为空dict
            if raw is not None and isinstance(raw, dict):
                default_config = raw

    # 加载环境配置
    config_file = f'config-{env}.yml'
    if not os.path.exists(config_file):
        raise FileNotFoundError(f"配置文件 {config_file} 不存在")

    with open(config_file, 'r', encoding='utf-8') as f:
        env_raw = yaml.safe_load(f)
        env_config = env_raw if (env_raw is not None and isinstance(env_raw, dict)) else {}

    # 环境配置覆盖默认配置
    config = merge_dicts(default_config, env_config)
    logger.info(f"config 内容如下： {config}")
    return config
