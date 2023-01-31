function start() {
  class NewComp extends React.Component {
    constructor(props) {
      super(props);
      this.estado = this.props.heading;
      this.headingStyle = {
        color: "red"
      };
    }
    static propTypes = {
      descricao: PropTypes.number
    };
    render() {
      return /*#__PURE__*/React.createElement("li", null, /*#__PURE__*/React.createElement("h2", {
        style: this.headingStyle
      }, this.estado), /*#__PURE__*/React.createElement("a", {
        style: this.props.href
      }, this.props.descricao), /*#__PURE__*/React.createElement("button", {
        color: "red",
        onClick: () => {
          this.estado = this.estado + "---MUDOU!";
          this.setState({});
        }
      }, "Click Me"), ";");
    }
  }
  const rootElement = React.createElement("div", {}, React.createElement("h1", {}, "Highest Level Heading"), React.createElement("ul", {}, React.createElement(NewComp, {
    heading: "First item, with second level heading",
    href: "https://www.ualg.pt",
    descricao: "UAlg web page"
  }), React.createElement(NewComp, {
    heading: "Second item, with second level heading",
    href: "https://www.ceot.ualg.pt/research-groups/networking-iot",
    descricao: "CEOT web page"
  })));
  ReactDOM.render(rootElement, document.getElementById("mainContainer"));
}

// return <li>
// <h2 className={headingStyle}>{estado}</h2>
// <a className={props.href}>{props.descricao}</a>
// const button = <button color="red"
//     onClick="alert ('clicked');">
//     Click Me
// </button>;
// </li>;
