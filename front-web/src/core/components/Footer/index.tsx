import './styles.scss';

function Footer() {
    return (
        <footer className="main-footer">
                Vex Canecas Personalizadas | Sistema Desenvolvido por: 
                <a className="footer-text" href="https://www.linkedin.com/in/jonathancomarella/" target="_new">
                       Jonathan Comarella
                </a>
            {/* <div className="footer-icons">
                <a href="https://www.youtube.com/channel/UCBVRGX-6zlDROUJjxNIpGkw" target="_new">
                    <YoutubeIcon />
                </a>

                <a href="https://www.linkedin.com/in/jonathancomarella/" target="_new">
                    <LinkedinIcon />
                </a>

                <a href="https://www.instagram.com/comarellaj" target="_new">
                    <InstagramIcon />
                </a>
            </div> */}
        </footer>
    )
}

export default Footer;