import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import LoadingGif from '../../images/loader.gif';
import NoLoadingGif from '../../images/noloader.gif';
import './styles/infinityScroll.css';


function topPosition(domElt) {
    if (!domElt) {
        return 0;
    }
    return domElt.offsetTop + topPosition(domElt.offsetParent);
}

class InfiniteScroll extends PureComponent {

    // globally: true for window scroll, false - for first child element scroll
    static propTypes = {
        pageStart: PropTypes.number.isRequired,
        threshold: PropTypes.number,
        loadNextPage: PropTypes.func,
        startLoadNextPage: PropTypes.func,
        hasMore: PropTypes.bool,
        loading: PropTypes.bool.isRequired,
        children: PropTypes.node.isRequired,
        globally: PropTypes.bool
    };

    static defaultProps = {
        pageStart: 0,
        hasMore: false,
        loadNextPage() {
        },
        startLoadNextPage() {
        },
        threshold: 250,
        loading: false,
        globally: true
    };
    scrollListener = () => {
        let threshold;
        let originalHeight;
        let bottomScrollCoord;

        if (this.props.globally) {
            threshold = Number(this.props.threshold);
            const scrollTop = this.getWindowScrollTop();
            bottomScrollCoord = scrollTop + window.innerHeight;
            const listAbsoluteTopCoord = topPosition(this.list);
            originalHeight = listAbsoluteTopCoord + this.list.offsetHeight;
        } else {
            originalHeight = this.list.scrollHeight;
            bottomScrollCoord = this.list.offsetHeight + this.list.scrollTop;
            threshold = 0;
        }
        if (originalHeight - bottomScrollCoord <= threshold) {
            this.handleLoadNextPage();
        }
    };

    componentDidMount() {
        if (this.props.globally) {
            this.list = this.domElement;
            this.listenerElement = window;
        } else {
            this.list = this.domElement.firstElementChild;
            this.listenerElement = this.domElement.firstElementChild;
        }
        this.attachScrollListener();
    }

    componentDidUpdate(prevProps) {
        if (prevProps && (prevProps.hasMore !== this.props.hasMore || prevProps.pageStart !== this.props.pageStart)) {
            this.scrollListener();
        }
    }

    componentWillUnmount() {
        this.detachScrollListener();
    }

    getWindowScrollTop() {
        return (window.pageYOffset === undefined)
            ? (document.documentElement || document.body.parentNode || document.body).scrollTop
            : window.pageYOffset;
    }

    handleLoadNextPage() {
        // Without this check we attach back scroll listener when we add loading animation. So we load page two times.
        if (this.props.loading) {
            return
        }
        console.log('Scrolled to bottom! Loading new items...');
        // This function is needed to immediately turn flag in store to true to stop loading
        this.props.startLoadNextPage();
        // call handleLoadNextPage after detachScrollListener to allow
        // for non-async handleLoadNextPage functions
        // @TODO: review timeout
        if (this.props.hasMore) {
            this.props.loadNextPage();
        }
    }

    attachScrollListener() {
        this.listenerElement.addEventListener('scroll', this.scrollListener);
        this.listenerElement.addEventListener('resize', this.scrollListener);
    }

    detachScrollListener() {
        this.listenerElement.removeEventListener('scroll', this.scrollListener);
        this.listenerElement.removeEventListener('resize', this.scrollListener);
    }

    render() {
        const loadingGif = (
            <div id="cc-loading-gif">
                <img src={LoadingGif}/>
            </div>);

        const loadingGifPlaceholder = (
            <div id="cc-loading-gif">
                <img src={NoLoadingGif}/>
                <span>Scroll down to see more</span>
            </div>);

        return (
            <div ref={domElement => this.domElement = domElement}>
                {this.props.children}
                {this.props.loading ? loadingGif : (this.props.hasMore ? loadingGifPlaceholder : '')}
            </div>
        );
    }
}

export default InfiniteScroll;

