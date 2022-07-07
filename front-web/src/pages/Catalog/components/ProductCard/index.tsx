import React from 'react';

import ProductPrice from 'core/components/ProductPrice';
import { Product } from 'core/types/Product';
import './styles.scss';

type Props ={
  product: Product;
}

const ProductCard = ({ product }: Props) =>(
  <div className="product-card-container">
  <div className="card-base border-radius-10 product-card">
    <img src={product.imgUrl} alt={product.name} className="product-card-image"/>
    <div className="product-info">
      <h6 className="product-name">
        {product.name}
      </h6>
    </div>
   <ProductPrice price={product.price}/>
  </div>
  </div>
);

export default ProductCard;