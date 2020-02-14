import { createAPI } from '@/utils/request';

const api = '/sys/permission';
export const list = data => createAPI('/sys/permission', 'get', data);
export const add = data => createAPI('/sys/permission', 'post', data);
export const update = data => createAPI(`/sys/permission/${data.id}`, 'put', data);
export const remove = data => createAPI(`/sys/permission/${data.id}`, 'delete', data);
export const detail = data => createAPI(`/sys/permission/${data.id}`, 'get', data);
export const saveOrUpdate = data => { return data.id ? update(data) : add(data); };
