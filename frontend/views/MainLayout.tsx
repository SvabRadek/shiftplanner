import {AppLayout} from '@hilla/react-components/AppLayout.js';
import {DrawerToggle} from '@hilla/react-components/DrawerToggle.js';
import Placeholder from 'Frontend/components/placeholder/Placeholder.js';
import {useRouteMetadata} from 'Frontend/util/routing.js';
import {Suspense} from 'react';
import {NavLink, Outlet} from 'react-router-dom';
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {useAuth} from "Frontend/auth";
import {SecondaryButton} from "Frontend/components/SecondaryButton";
import {Icon} from "@hilla/react-components/Icon";

const navLinkClasses = ({isActive}: any) => {
    return `block rounded-m p-s ${isActive ? 'bg-primary-10 text-primary' : 'text-body'}`;
};

export default function MainLayout() {
    const currentTitle = useRouteMetadata()?.title ?? 'Planovac';
    const auth = useAuth();
    return (
        <AppLayout primarySection="drawer">
            <div slot="drawer" className="flex flex-col justify-between h-full p-m">
                <header className="flex flex-col gap-m">
                    <h1 className="text-l m-0">Planovac</h1>
                    <nav>
                        <NavLink className={navLinkClasses} to="/">
                            Rozvrh
                        </NavLink>
                        <NavLink className={navLinkClasses} to="/zamestnanci">
                            Zaměstnanci
                        </NavLink>
                        <NavLink className={navLinkClasses} to="/test">
                            Test
                        </NavLink>
                    </nav>
                </header>
            </div>

            <DrawerToggle slot="navbar" aria-label="Menu toggle"></DrawerToggle>
            <h2 slot="navbar" className="text-l m-0">
                {currentTitle}
            </h2>
            <HorizontalLayout slot={"navbar"} theme={"spacing padding"}
                              style={{ width: "100%", justifyContent: "end", alignItems: "baseline"}}>
                <HorizontalLayout theme={"spacing"}>
                </HorizontalLayout>
                <HorizontalLayout theme={"spacing"}>
                    <h5>{auth.state.user?.username}</h5>
                </HorizontalLayout>
                <a href={"/login"}>
                    <SecondaryButton onClick={() => auth.logout()}>
                        <Icon icon={"vaadin:exit"} slot={"prefix"}/>
                        Logout
                    </SecondaryButton>
                </a>
            </HorizontalLayout>

            <Suspense fallback={<Placeholder/>}>
                <Outlet/>
            </Suspense>
        </AppLayout>
    );
}
