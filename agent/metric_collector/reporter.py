import requests


def report_metric(metric_data: dict, report_url: str):
    """上报指标到平台接口，返回response文本+http_code"""
    try:
        resp = requests.post(
            report_url,
            json=metric_data,
            timeout=10
        )
        return {
            "success": True,
            "http_code": resp.status_code,
            "response_text": resp.text
        }
    except Exception as e:
        return {
            "success": False,
            "error": str(e)
        }
