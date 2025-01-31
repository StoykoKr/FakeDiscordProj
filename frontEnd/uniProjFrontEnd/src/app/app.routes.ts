import { Routes } from '@angular/router';
import { UserPage } from './features/user/user.component';
import { ChannelPage } from './features/channel/channel.component';
import { ChatPage } from './features/chat/chat.component';

export const routes: Routes = [
{
    path: '',
    redirectTo: 'users',
    pathMatch:'full'
},
{
    path: 'users',
    component: UserPage
},
{
    path:"channels",
    component: ChannelPage
},
{
    path:"users/:id",
    component: UserPage    
},
{
    path:"users/:id/friends",
    component: UserPage    
},
{
    path:"users/:id/friends/:userChatId/chat",
    component: ChatPage
},
{
    path:"users/:id/channels/:channelChatId/chat",
    component: ChatPage
},
{
    path:"users/:id/channels",
    component: ChannelPage
}

];
