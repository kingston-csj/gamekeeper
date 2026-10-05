import logging

from apscheduler.schedulers.background import BackgroundScheduler
from metric_collector.collector import collect_metrics
from metric_collector.reporter import report_metric

def metric_job(report_url):
    """定时采集+上报主任务"""
    metric = collect_metrics()
    ret = report_metric(metric, report_url)

    logging.info("==================== 监控采集上报 ====================")
    logging.info(f"主机信息: {metric['hostName']} ({metric['hostIp']})")
    logging.info(f"CPU 使用率: {metric['cpuUsage']}%")
    logging.info(f"内存使用率: {metric['memUsage']}%  (总量: {metric['memTotal']} KB / 已用: {metric['memUsed']} KB)")
    logging.info(f"磁盘使用率: {metric['diskUsage']}%  (总量: {metric['diskTotal']} KB / 已用: {metric['diskUsed']} KB)")
    logging.info(f"系统负载  : 1min={metric['load1']}  5min={metric['load5']}  15min={metric['load15']}")
    logging.info(f"上报地址  : {report_url}")
    logging.info(f"上报结果  : {ret}")
    logging.info("======================================================\n")

def start_scheduler(report_url):
    scheduler = BackgroundScheduler()
    metric_interval_seconds = 1800
    scheduler.add_job(
        func= metric_job,
        trigger="interval",
        seconds=metric_interval_seconds,
        args=[report_url],
        id="metric_collect_job",
        replace_existing=True
    )
    scheduler.start()
    logging.info(f"指标定时采集已启动，间隔 {metric_interval_seconds}s")
    return scheduler
