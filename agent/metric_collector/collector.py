import psutil
import socket
import platform

def get_host_info():
    hostname = platform.node()
    host_ip = "127.0.0.1"
    for if_name, addrs in psutil.net_if_addrs().items():
        for addr in addrs:
            if addr.family == socket.AF_INET and not addr.address.startswith("127."):
                host_ip = addr.address
                return host_ip, hostname
    return host_ip, hostname

def collect_metrics():
    host_ip, host_name = get_host_info()
    is_windows = platform.system() == "Windows"

    cpu_usage = psutil.cpu_percent(interval=1)

    # 内存
    mem = psutil.virtual_memory()
    mem_total_kb = mem.total // 1024
    mem_used_kb = mem.used // 1024
    mem_usage = mem.percent

    # 磁盘：Windows取C盘，Linux取/
    if is_windows:
        disk_path = "C:\\"
    else:
        disk_path = "/"
    disk = psutil.disk_usage(disk_path)
    disk_total_kb = disk.total // 1024
    disk_used_kb = disk.used // 1024
    disk_usage = disk.percent

    # loadavg Windows不存在，填充0
    if is_windows:
        load1, load5, load15 = 0.0, 0.0, 0.0
    else:
        load1, load5, load15 = psutil.getloadavg()

    return {
        "hostIp": host_ip,
        "hostName": host_name,
        "cpuUsage": round(cpu_usage, 2),
        "memUsage": round(mem_usage, 2),
        "diskUsage": round(disk_usage, 2),
        "memTotal": mem_total_kb,
        "memUsed": mem_used_kb,
        "diskTotal": disk_total_kb,
        "diskUsed": disk_used_kb,
        "load1": load1,
        "load5": load5,
        "load15": load15
    }
