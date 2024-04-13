/* 
 * 菜单管理模块
 */

// 获取导航菜单树
export function findNavTree() {
  const navTreeData = {
    
  }
  return {
    url: 'menu/findMenuTree',
    type: 'get',
    data: menuTreeData
  }
}

export function setParentName(data) {
  if(data == null) {
    return
  }
  let len = data.length
  for(let i=0; i<len; i++) {
    let menu = data[i]
    menu.parentName = 'menu' + menu.parentId
    if(menu.children != null) {
      setParentName(menu.children)
    }
  }
}

