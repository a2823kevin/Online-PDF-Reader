import MenuToggle from "@/components/menuToggle";
import PageIndicator from "@/components/pageIndicator";
import Account from "@/components/account";
import Logo from "@/components/logo";

const Header = (props) => {
    return (
        <div className="fixed top:0 left:0 right:0 h:10vh pl:1vw flex justify-content:space-between bg:#8B0000 color:white f:Arial, Helvetica, sans-serif f:bold">
            <div className="flex align-items:center gap:1vw h:100%">
                <MenuToggle />
                <Logo />
            </div>
            <PageIndicator index={props.index} />
            <Account />
        </div>
    )
};

export default Header;